/*
 * Copyright 2016 McDowell
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.kludje;

import uk.kludje.property.*;

import java.util.Objects;

/**
 * <p>Provides a basic meta-method builder for common {@code Object} method implementations.</p>
 * <p>Example builds equals, hashCode and toString methods using the "id", "name" and "dateOfBirth" properties:</p>
 * <pre><code>
 * import uk.kludje.Meta;
 * import java.time.LocalDate;
 * import static uk.kludje.Meta.meta;
 *
 * public class PersonPojo {
 *   private static final Meta&lt;PersonPojo&gt; META = meta(PersonPojo.class)
 *                              .longs(pp -&gt; pp.id)
 *                              .objects(pp -&gt; pp.name, pp -&gt; pp.dateOfBirth);
 *
 *   private final long id;
 *   private final String name;
 *   private final LocalDate dateOfBirth;
 *
 *   public PersonPojo(long id, String name, LocalDate dateOfBirth) {
 *     this.id = id;
 *     this.name = name;
 *     this.dateOfBirth = dateOfBirth;
 *   }
 *
 *   public String getName() {
 *     return name;
 *   }
 *
 *   public LocalDate getDateOfBirth() {
 *     return dateOfBirth;
 *   }
 *
 *   public boolean equals(Object obj) {
 *     return META.equals(this, obj);
 *   }
 *
 *   public int hashCode() {
 *     return META.hashCode(this);
 *   }
 *
 *   public String toString() {
 *     return META.toString(this);
 *   }
 * }
 * </code></pre>
 * <p>
 * <p>Note: arrays are treated as objects by default; see {@link MetaConfig#withShallowArraySupport()}.
 * If classes can be equal to subtypes use {@link MetaConfig#withInstanceofEqualsTypeCheck()}.</p>
 * <p>
 * <p>Instances of this type are immutable and thread safe.</p>
 *
 * @param <T> the accessed type
 * @see PropertyGetterList
 */
public final class Meta<T> extends PropertyGetterList<T, Meta<T>> {

  private static final ArrayMapper<TypedProperty> TYPED_PROPERTY_ARRAYS = ArrayMapper.arrayMapper(TypedProperty.class);
  private static final ArrayMapper<String> TYPED_STRING_ARRAYS = ArrayMapper.arrayMapper(String.class);

  private final MetaConfig config;
  private final Class<T> type;
  private final TypedProperty[] props;
  private final String[] names;
  private final MetaConfig.InstanceCheckPolicy instancePolicy;
  private final MetaConfig.ObjectEqualsPolicy equalsPolicy;
  private final MetaConfig.ObjectHashCodePolicy hashcodePolicy;
  private final MetaConfig.ObjectToStringPolicy toStringPolicy;

  private Meta(MetaConfig config,
               Class<T> type,
               TypedProperty[] props,
               String[] names) {
    this.config = config;
    this.type = type;
    this.props = props;
    this.names = names;
    this.instancePolicy = config.getInstanceCheckPolicy();
    this.equalsPolicy = config.getObjectEqualsPolicy();
    this.hashcodePolicy = config.getObjectHashCodePolicy();
    this.toStringPolicy = config.getObjectToStringPolicy();
  }

  @Override
  protected Meta<T> newInstance(Meta<T> old, String name, TypedProperty getter) {
    Fatal.when(getter == null, "getter == null");
    Fatal.when(name == null, "name == null");

    TypedProperty[] newProps = TYPED_PROPERTY_ARRAYS.concat(old.props, getter);
    String[] newNames = TYPED_STRING_ARRAYS.concat(old.names, name);

    return new Meta<>(old.config, old.type, newProps, newNames);
  }

  /**
   * @param type the class of type T
   * @param <T>  the type
   * @return a new instance with no properties
   */
  public static <T> Meta<T> meta(Class<T> type) {
    Fatal.when(type == null, "type == null");

    return new Meta<>(MetaConfig.defaultConfig(), type, TYPED_PROPERTY_ARRAYS.empty(), TYPED_STRING_ARRAYS.empty());
  }

  /**
   * WARNING: when this method is used a subclass can never be equal to a parent type.
   *
   * @param <T> the type of class
   * @return a new instance
   */
  public static <T> Meta<T> meta() {
    return new Meta<>(MetaConfig.defaultConfig(), null, TYPED_PROPERTY_ARRAYS.empty(), TYPED_STRING_ARRAYS.empty());
  }

  /**
   * Some aspects of the provided methods are caller-configurable.
   * Use this method to alter these aspects of the configuration.
   * <p>
   * If {@link #meta()} was used to instantiate the instance any changes to {@link MetaConfig.ObjectEqualsPolicy}
   * will result in an error. Use {@link #meta(Class)} instead.
   *
   * @param config the new configuration; must not be null
   * @return a new instance with the new configuration
   */
  public Meta<T> configure(MetaConfig config) {
    Fatal.when(config == null, "config == null");

    //noinspection ConstantConditions
    if (type == null && (config.getInstanceCheckPolicy() != MetaConfig.defaultConfig().getInstanceCheckPolicy())) {
      String message = "Not enough type information to guarantee equals contract. Use Meta.meta(Class) to construct this type instead.";
      throw new AssertionError(message);
    }

    return new Meta<>(config, this.type, this.props, this.names);
  }

  /**
   * {@code any} is equal to {@code t} if it is of type {@code T}
   * and all the properties defined by this type are equal.
   *
   * @param t   a non-null instance of type T
   * @param any any object, including null
   * @return true if the arguments are equal
   */
  @SuppressWarnings("unchecked")
  public boolean equals(T t, Object any) {
    Objects.requireNonNull(t);
    if (any == null) {
      return false;
    }
    if (any == t) {
      return true;
    }
    if (!this.instancePolicy.isSameType(type == null ? t.getClass() : type, any)) {
      return false;
    }

    T other = (T) any;
    for (TypedProperty p : props) {
      PropertyType type = p.type();

      switch (type) {
        case INT:
          IntGetter<T> ig = (IntGetter<T>) p;
          if (ig.getInt(t) != ig.getInt(other)) {
            return false;
          }
          break;
        case OBJECT:
          Getter<T> g = (Getter<T>) p;
          Object o1 = g.get(t);
          Object o2 = g.get(other);
          if (!equalsPolicy.areEqual(o1, o2)) {
            return false;
          }
          break;
        case BOOLEAN:
          BooleanGetter<T> bg = (BooleanGetter<T>) p;
          if (bg.getBoolean(t) != bg.getBoolean(other)) {
            return false;
          }
          break;
        case LONG:
          LongGetter<T> lg = (LongGetter<T>) p;
          if (lg.getLong(t) != lg.getLong(other)) {
            return false;
          }
          break;
        case CHAR:
          CharGetter<T> cg = (CharGetter<T>) p;
          if (cg.getChar(t) != cg.getChar(other)) {
            return false;
          }
          break;
        case DOUBLE:
          DoubleGetter<T> dg = (DoubleGetter<T>) p;
          if (dg.getDouble(t) != dg.getDouble(other)) {
            return false;
          }
          break;
        case FLOAT:
          FloatGetter<T> fg = (FloatGetter<T>) p;
          if (fg.getFloat(t) != fg.getFloat(other)) {
            return false;
          }
          break;
        case SHORT:
          ShortGetter<T> sg = (ShortGetter<T>) p;
          if (sg.getShort(t) != sg.getShort(other)) {
            return false;
          }
          break;
        case BYTE:
          ByteGetter<T> byg = (ByteGetter<T>) p;
          if (byg.getByte(t) != byg.getByte(other)) {
            return false;
          }
          break;
        default:
          throw new AssertionError("Unsupported PropertyType");
      }
    }
    return true;
  }

  /**
   * Creates a hash of all values defined by the properties provided to this instance.
   * The hashing formula is not specified.
   *
   * @param t the non-null instance to create a hash for
   * @return the hash
   */
  @SuppressWarnings("unchecked")
  public int hashCode(T t) {
    Objects.requireNonNull(t);

    int result = 0;
    int prime = 31;

    for (TypedProperty p : props) {
      result = result * prime;

      PropertyType type = p.type();

      switch (type) {
        case INT:
          IntGetter<T> ig = (IntGetter<T>) p;
          result += ig.getInt(t);
          break;
        case OBJECT:
          Getter<T> g = (Getter<T>) p;
          result += hashcodePolicy.toHashcode(g.get(t));
          break;
        case BOOLEAN:
          BooleanGetter<T> bg = (BooleanGetter<T>) p;
          result += bg.getBoolean(t) ? 1 : 0;
          break;
        case LONG:
          LongGetter<T> lg = (LongGetter<T>) p;
          result += Long.hashCode(lg.getLong(t));
          break;
        case CHAR:
          CharGetter<T> cg = (CharGetter<T>) p;
          result += cg.getChar(t);
          break;
        case DOUBLE:
          DoubleGetter<T> dg = (DoubleGetter<T>) p;
          result += Double.hashCode(dg.getDouble(t));
          break;
        case FLOAT:
          FloatGetter<T> fg = (FloatGetter<T>) p;
          result += Float.hashCode(fg.getFloat(t));
          break;
        case SHORT:
          ShortGetter<T> sg = (ShortGetter<T>) p;
          result += sg.getShort(t);
          break;
        case BYTE:
          ByteGetter<T> byg = (ByteGetter<T>) p;
          result += byg.getByte(t);
          break;
        default:
          throw new AssertionError("Unsupported PropertyType");
      }
    }
    return result;
  }

  /**
   * Creates a string form of the type for debugging purposes.
   * The exact form is not specified.
   *
   * @param t the non-null instance to create a string form of
   * @return debug string
   */
  @SuppressWarnings("unchecked")
  public String toString(T t) {
    Class<?> tClass = t.getClass();

    StringBuilder buf = new StringBuilder();
    buf.append(tClass.getSimpleName());
    buf.append(" {");

    String delim = "";

    for (int i = 0; i < props.length; i++) {

      buf.append(delim);
      delim = ", ";

      TypedProperty p = props[i];
      String name = names[i];

      if (!"".equals(name)) {
        buf.append(name);
        buf.append('=');
      }

      PropertyType type = p.type();
      switch (type) {
        case INT:
          IntGetter<T> ig = (uk.kludje.property.IntGetter<T>) p;
          buf.append(ig.getInt(t));
          break;
        case OBJECT:
          uk.kludje.property.Getter<T> g = (Getter<T>) p;
          String str = this.toStringPolicy.toString(g.apply(t));
          buf.append(str);
          break;
        case BOOLEAN:
          BooleanGetter<T> bg = (BooleanGetter<T>) p;
          buf.append(bg.getBoolean(t));
          break;
        case LONG:
          LongGetter<T> lg = (LongGetter<T>) p;
          buf.append(lg.getLong(t));
          break;
        case CHAR:
          CharGetter<T> cg = (CharGetter<T>) p;
          buf.append(cg.getChar(t));
          break;
        case DOUBLE:
          DoubleGetter<T> dg = (DoubleGetter<T>) p;
          buf.append(dg.getDouble(t));
          break;
        case FLOAT:
          FloatGetter<T> fg = (FloatGetter<T>) p;
          buf.append(fg.getFloat(t));
          break;
        case SHORT:
          ShortGetter<T> sg = (ShortGetter<T>) p;
          buf.append(sg.getShort(t));
          break;
        case BYTE:
          ByteGetter<T> byg = (ByteGetter<T>) p;
          buf.append(byg.getByte(t));
          break;
        default:
          throw new AssertionError("Unsupported PropertyType");
      }
    }

    return buf.append('}').toString();
  }

  /**
   * @return the number of properties this instance exposes
   */
  public int size() {
    return props.length;
  }

  /**
   * @param index the getter to return
   * @return an object that can be cast to a getter type
   * @see uk.kludje.property.Getter
   * @see uk.kludje.property.BooleanGetter
   * @see uk.kludje.property.ByteGetter
   * @see uk.kludje.property.ShortGetter
   * @see uk.kludje.property.IntGetter
   * @see uk.kludje.property.LongGetter
   * @see uk.kludje.property.FloatGetter
   * @see uk.kludje.property.DoubleGetter
   * @see uk.kludje.property.CharGetter
   * @see PropertyType
   */
  public TypedProperty propertyAt(int index) {
    return props[index];
  }

  /**
   * @param index the name index
   * @return the property name for the given index or the empty string
   */
  public String nameAt(int index) {
    return names[index];
  }
}
