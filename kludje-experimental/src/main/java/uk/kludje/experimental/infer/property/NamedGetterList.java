/*
Copyright 2016 McDowell

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package uk.kludje.experimental.infer.property;

import uk.kludje.Ensure;
import uk.kludje.Meta;
import uk.kludje.experimental.infer.io.SerializedLambdas;
import uk.kludje.fn.nary.TriFunction;
import uk.kludje.property.Getter;
import uk.kludje.property.PropertyGetterList;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;

/**
 * <p>This type aims to reduce the verbosity of using {@link Meta#toString(Object)} by inferring names namer method references.
 * This type has strict usage requirements.</p>
 *
 * <pre>
 * class Pojo {
 *
 *   private static final Meta&lt;Pojo> META = NamedGetterList.namer(Meta.meta(Pojo.class))
 *                                                         .objects(Pojo::getStringProp)
 *                                                         .list();
 *
 *   String stringProp;
 *
 *   String getStringProp() {
 *     return stringProp;
 *   }
 *
 *   @Override
 *   public boolean equals(Object obj) {
 *     return META.equals(this, obj);
 *   }
 *
 *   @Override
 *   public int hashCode() {
 *     return META.hashCode(this);
 *   }
 *
 *   @Override
 *   public String toString() {
 *     return META.toString(this);
 *   }
 * }
 * </pre>
 *
 * The above code will infer the name {@code stringProp} using reflection on the method reference {@code Pojo::getStringProp}.
 *
 * @param <T> the accessed type
 * @see SerializedLambdas#toSerializedLambda(Serializable)
 * @see SerializedLambda#getImplMethodName()
 * @see Meta
 */
public final class NamedGetterList<T, PGL extends PropertyGetterList<T, PGL>> {

  private final PGL propertyGetterList;

  private NamedGetterList(PGL propertyGetterList) {
    this.propertyGetterList = propertyGetterList;
  }

  public PGL list() {
    return propertyGetterList;
  }

  /**
   * When {@link #objects(SerializableGetter[])} is invoked, resolves the method name and calls
   * {@link PropertyGetterList#namedObject(String, Getter)} on the given argument.
   * Similar rules apply for the primitive types.
   *
   * @param propertyGetterList the instance to add named properties to
   * @param <T> the property source type
   * @param <PGL> the PropertyGetterList type
   * @return a new instance
   */
  public static <T, PGL extends PropertyGetterList<T, PGL>> NamedGetterList<T, PGL> namer(PGL propertyGetterList) {
    Ensure.that(propertyGetterList != null, "propertyGetterList != null");

    return new NamedGetterList<>(propertyGetterList);
  }

  /**
   * @param getters must be method references of the form Foo::getBar
   * @return a new instance
   */
  @SafeVarargs
  public final NamedGetterList<T, PGL> objects(SerializableGetter<T>... getters) {
    return toNamed(getters, PropertyGetterList::namedObject);
  }

  /**
   * @param getters must be method references of the form Foo::getBar
   * @return a new instance
   */
  @SafeVarargs
  public final NamedGetterList<T, PGL> booleans(SerializableBooleanGetter<T>... getters) {
    return toNamed(getters, PropertyGetterList::namedBoolean);
  }

  /**
   * @param getters must be method references of the form Foo::getBar
   * @return a new instance
   */
  @SafeVarargs
  public final NamedGetterList<T, PGL> chars(SerializableCharGetter<T>... getters) {
    return toNamed(getters, PropertyGetterList::namedChar);
  }

  /**
   * @param getters must be method references of the form Foo::getBar
   * @return a new instance
   */
  @SafeVarargs
  public final NamedGetterList<T, PGL> bytes(SerializableByteGetter<T>... getters) {
    return toNamed(getters, PropertyGetterList::namedByte);
  }

  /**
   * @param getters must be method references of the form Foo::getBar
   * @return a new instance
   */
  @SafeVarargs
  public final NamedGetterList<T, PGL> shorts(SerializableShortGetter<T>... getters) {
    return toNamed(getters, PropertyGetterList::namedShort);
  }

  /**
   * @param getters must be method references of the form Foo::getBar
   * @return a new instance
   */
  @SafeVarargs
  public final NamedGetterList<T, PGL> ints(SerializableIntGetter<T>... getters) {
    return toNamed(getters, PropertyGetterList::namedInt);
  }

  /**
   * @param getters must be method references of the form Foo::getBar
   * @return a new instance
   */
  @SafeVarargs
  public final NamedGetterList<T, PGL> longs(SerializableLongGetter<T>... getters) {
    return toNamed(getters, PropertyGetterList::namedLong);
  }

  /**
   * @param getters must be method references of the form Foo::getBar
   * @return a new instance
   */
  @SafeVarargs
  public final NamedGetterList<T, PGL> floats(SerializableFloatGetter<T>... getters) {
    return toNamed(getters, PropertyGetterList::namedFloat);
  }

  /**
   * @param getters must be method references of the form Foo::getBar
   * @return a new instance
   */
  @SafeVarargs
  public final NamedGetterList<T, PGL> doubles(SerializableDoubleGetter<T>... getters) {
    return toNamed(getters, PropertyGetterList::namedDouble);
  }

  /**
   * @param getters   the property getters
   * @param transform applies the new name and property to the old getter container
   * @param <G>       the getter type
   * @return the new namer
   */
  private <G extends Serializable> NamedGetterList<T, PGL> toNamed(G[] getters, TriFunction<PGL, String, G, PGL> transform) {
    PGL meta = this.propertyGetterList;
    for (G prop : getters) {
      String name = toName(prop);
      meta = transform.apply(meta, name, prop);
    }
    return namer(meta);
  }

  private String toName(Serializable property) {
    SerializedLambda lambda = SerializedLambdas.toSerializedLambda(property);
    String methodName = lambda.getImplMethodName();

    if (methodName.matches("^get\\p{javaUpperCase}.*")) {
      char start = methodName.charAt(3);
      start = Character.toLowerCase(start);
      return start + methodName.substring(4);
    }

    return methodName;
  }
}
