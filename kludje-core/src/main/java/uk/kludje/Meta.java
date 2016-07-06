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

import java.util.Objects;

/**
 * <p>Provides a basic meta-method builder for common {@code Object} method implementations.</p>
 * <p>
 * <p>Example checked builds equals, hashCode and toString methods using the id, name and dateOfBirth properties:</p>
 * <p>
 * <pre>
 * import uk.kludje.Meta;
 * import java.time.LocalDate;
 * import static uk.kludje.Meta.meta;
 *
 * public class PersonPojo {
 *   private static final Meta&lt;PersonPojo&gt; META = meta(PersonPojo.class)
 * .                            longs($ -&gt; $.id).objects($ -&gt; $.name, $ -&gt; $.dateOfBirth);
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
 * </pre>
 * <p>
 * <p>Note: arrays are treated as objects; use a decorator to provide alternative equals/hashCode/toString behaviour.
 * For example, Google Guava's {@code Bytes.asList(byte...)}.</p>
 * <p>
 * <p>Instances of this type are immutable and thread safe.</p>
 *
 * @param <T> the accessed type
 */
public final class Meta<T> {

  private final Class<T> type;
  private final TypedProperty[] props;
  private final String[] names;

  @SuppressWarnings("unchecked")
  private Meta(Class<T> type,
               TypedProperty[] props,
               String[] names) {
    this.type = type;
    this.props = props;
    this.names = names;
  }

  private static <T> Meta<T> newMeta(Meta<T> old, String name, TypedProperty getter) {
    Ensure.that(getter != null, "getter != null");
    Ensure.that(name != null, "name != null");

    TypedProperty[] newProps = new TypedProperty[old.props.length + 1];
    System.arraycopy(old.props, 0, newProps, 0, old.props.length);
    newProps[old.props.length] = getter;

    String[] newNames = new String[old.names.length + 1];
    System.arraycopy(old.names, 0, newNames, 0, old.names.length);
    newNames[old.names.length] = name;

    return new Meta<T>(old.type, newProps, newNames);
  }

  /**
   * WARNING: this method will be removed
   *
   * @param <T> the type of class
   * @return a new instance
   * @deprecated does not retain enough type information for subclasses; use meta(Class) instead
   * @see #meta(Class)
   */
  @Deprecated
  public static <T> Meta<T> meta() {
    return new Meta<>(null, new TypedProperty[0], new String[0]);
  }

  /**
   * @param type the class of type T
   * @param <T> the type
   * @return a new instance with no properties
   */
  public static <T> Meta<T> meta(Class<T> type) {
    Ensure.that(type != null, "type != null");

    return new Meta<>(type, new TypedProperty[0], new String[0]);
  }

  /**
   * @param name   a property name for toString() generation; must not be null
   * @param getter the value retriever
   * @return a new instance with the property appended
   */
  public Meta<T> namedObject(String name, Getter<T> getter) {
    return newMeta(this, name, getter);
  }

  /**
   * @param name   a property name for toString() generation; must not be null
   * @param getter the value retriever
   * @return a new instance with the property appended
   */
  public Meta<T> intProperty(String name, IntGetter<T> getter) {
    return newMeta(this, name, getter);
  }

  /**
   * @param name   a property name for toString() generation; must not be null
   * @param getter the value retriever
   * @return a new instance with the property appended
   */
  public Meta<T> namedBoolean(String name, BooleanGetter<T> getter) {
    return newMeta(this, name, getter);
  }

  /**
   * @param name   a property name for toString() generation; must not be null
   * @param getter the value retriever
   * @return a new instance with the property appended
   */
  public Meta<T> namedByte(String name, ByteGetter<T> getter) {
    return newMeta(this, name, getter);
  }

  /**
   * @param name   a property name for toString() generation; must not be null
   * @param getter the value retriever
   * @return a new instance with the property appended
   */
  public Meta<T> namedShort(String name, ShortGetter<T> getter) {
    return newMeta(this, name, getter);
  }

  /**
   * @param name   a property name for toString() generation; must not be null
   * @param getter the value retriever
   * @return a new instance with the property appended
   */
  public Meta<T> namedInt(String name, IntGetter<T> getter) {
    return newMeta(this, name, getter);
  }

  /**
   * @param name   a property name for toString() generation; must not be null
   * @param getter the value retriever
   * @return a new instance with the property appended
   */
  public Meta<T> namedLong(String name, LongGetter<T> getter) {
    return newMeta(this, name, getter);
  }

  /**
   * @param name   a property name for toString() generation; must not be null
   * @param getter the value retriever
   * @return a new instance with the property appended
   */
  public Meta<T> namedFloat(String name, FloatGetter<T> getter) {
    return newMeta(this, name, getter);
  }

  /**
   * @param name   a property name for toString() generation; must not be null
   * @param getter the value retriever
   * @return a new instance with the property appended
   */
  public Meta<T> namedDouble(String name, DoubleGetter<T> getter) {
    return newMeta(this, name, getter);
  }

  /**
   * @param name   a property name for toString() generation; must not be null
   * @param getter the value retriever
   * @return a new instance with the property appended
   */
  public Meta<T> namedChar(String name, CharGetter<T> getter) {
    return newMeta(this, name, getter);
  }

  /**
   * Use to specify properties of type object checked should be considered by this type.
   * <p>
   * Do not use this method for primitive properties - alternatives have been provided.
   * <p>
   * This method does not mutate the instance; it returns a new one.
   *
   * @param getters a vararg array of non-null getters
   * @return a new instance
   */
  @SafeVarargs
  public final Meta<T> objects(Getter<T>... getters) {
    Meta<T> result = this;
    for (Getter<T> g : getters) {
      result = newMeta(result, "", g);
    }
    return result;
  }

  @SafeVarargs
  public final Meta<T> booleans(BooleanGetter<T>... getters) {
    Meta<T> result = this;
    for (BooleanGetter<T> g : getters) {
      result = newMeta(result, "", g);
    }
    return result;
  }

  @SafeVarargs
  public final Meta<T> chars(CharGetter<T>... getters) {
    Meta<T> result = this;
    for (CharGetter<T> g : getters) {
      result = newMeta(result, "", g);
    }
    return result;
  }

  @SafeVarargs
  public final Meta<T> bytes(ByteGetter<T>... getters) {
    Meta<T> result = this;
    for (ByteGetter<T> g : getters) {
      result = newMeta(result, "", g);
    }
    return result;
  }

  @SafeVarargs
  public final Meta<T> shorts(ShortGetter<T>... getters) {
    Meta<T> result = this;
    for (ShortGetter<T> g : getters) {
      result = newMeta(result, "", g);
    }
    return result;
  }

  @SafeVarargs
  public final Meta<T> ints(IntGetter<T>... getters) {
    Meta<T> result = this;
    for (IntGetter<T> g : getters) {
      result = newMeta(result, "", g);
    }
    return result;
  }

  @SafeVarargs
  public final Meta<T> longs(LongGetter<T>... getters) {
    Meta<T> result = this;
    for (LongGetter<T> g : getters) {
      result = newMeta(result, "", g);
    }
    return result;
  }

  @SafeVarargs
  public final Meta<T> floats(FloatGetter<T>... getters) {
    Meta<T> result = this;
    for (FloatGetter<T> g : getters) {
      result = newMeta(result, "", g);
    }
    return result;
  }

  @SafeVarargs
  public final Meta<T> doubles(DoubleGetter<T>... getters) {
    Meta<T> result = this;
    for (DoubleGetter<T> g : getters) {
      result = newMeta(result, "", g);
    }
    return result;
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
    Class<T> tClass = (type == null) ? (Class<T>) t.getClass() : type;
    if (!tClass.isInstance(any)) {
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
          if (!Objects.equals(g.get(t), g.get(other))) {
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
          throw new AssertionError("Unsupported PropertyType: " + type);
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
          result += Objects.hashCode(g.get(t));
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
          IntGetter<T> ig = (IntGetter<T>) p;
          buf.append(ig.getInt(t));
          break;
        case OBJECT:
          Getter<T> g = (Getter<T>) p;
          buf.append(g.get(t));
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
   * @see Getter
   * @see BooleanGetter
   * @see ByteGetter
   * @see ShortGetter
   * @see IntGetter
   * @see LongGetter
   * @see FloatGetter
   * @see DoubleGetter
   * @see CharGetter
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


  /**
   * A functional interface for reading a property value.
   * Alternative types have been provided for primitives.
   *
   * @param <T> the type to read the property from
   * @see Meta#objects(Meta.Getter[])
   */
  @FunctionalInterface
  public interface Getter<T> extends TypedProperty {
    /**
     * Reads a property value from the argument.
     *
     * @param t the source of the property value
     * @return the property value instance or null
     */
    Object get(T t);

    /**
     * @return OBJECT
     */
    default PropertyType type() {
      return PropertyType.OBJECT;
    }
  }

  @FunctionalInterface
  public interface BooleanGetter<T> extends TypedProperty {
    boolean getBoolean(T t);

    /**
     * @return BOOLEAN
     */
    default PropertyType type() {
      return PropertyType.BOOLEAN;
    }
  }

  @FunctionalInterface
  public interface CharGetter<T> extends TypedProperty {
    char getChar(T t);

    /**
     * @return CHAR
     */
    default PropertyType type() {
      return PropertyType.CHAR;
    }
  }

  @FunctionalInterface
  public interface ByteGetter<T> extends TypedProperty {
    byte getByte(T t);

    /**
     * @return BYTE
     */
    default PropertyType type() {
      return PropertyType.BYTE;
    }
  }

  @FunctionalInterface
  public interface ShortGetter<T> extends TypedProperty {
    short getShort(T t);

    /**
     * @return SHORT
     */
    default PropertyType type() {
      return PropertyType.SHORT;
    }
  }

  @FunctionalInterface
  public interface IntGetter<T> extends TypedProperty {
    int getInt(T t);

    /**
     * @return INT
     */
    default PropertyType type() {
      return PropertyType.INT;
    }
  }

  @FunctionalInterface
  public interface LongGetter<T> extends TypedProperty {
    long getLong(T t);

    /**
     * @return LONG
     */
    default PropertyType type() {
      return PropertyType.LONG;
    }
  }

  @FunctionalInterface
  public interface FloatGetter<T> extends TypedProperty {
    float getFloat(T t);

    /**
     * @return FLOAT
     */
    default PropertyType type() {
      return PropertyType.FLOAT;
    }
  }

  @FunctionalInterface
  public interface DoubleGetter<T> extends TypedProperty {
    double getDouble(T t);

    /**
     * @return DOUBLE
     */
    default PropertyType type() {
      return PropertyType.DOUBLE;
    }
  }

  public interface TypedProperty {
    PropertyType type();
  }

  /**
   * Supported property types.
   */
  public enum PropertyType {
    OBJECT, BOOLEAN, CHAR, BYTE, SHORT, INT, LONG, FLOAT, DOUBLE
  }
}
