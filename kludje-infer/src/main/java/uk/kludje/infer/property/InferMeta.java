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

package uk.kludje.infer.property;

import uk.kludje.Ensure;
import uk.kludje.Meta;
import uk.kludje.fn.nary.TriFunction;
import uk.kludje.infer.io.SerializedLambdas;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;

/**
 * This type aims to reduce the verbosity of using {@link Meta#toString(Object)} by inferring names from method references.
 * This type has strict usage requirements.
 *
 * @param <T> the accessed type
 */
public final class InferMeta<T> {

  private final Meta<T> meta;

  private InferMeta(Meta<T> meta) {
    this.meta = meta;
  }

  public Meta<T> meta() {
    return meta;
  }

  public static <T> InferMeta<T> from(Meta<T> meta) {
    Ensure.that(meta != null, "meta != null");

    return new InferMeta<>(meta);
  }

  @SafeVarargs
  public final InferMeta<T> objects(SerializableGetter<T>... getters) {
    return toNamed(getters, Meta::namedObject);
  }

  @SafeVarargs
  public final InferMeta<T> booleans(SerializableBooleanGetter<T>... getters) {
    return toNamed(getters, Meta::namedBoolean);
  }

  @SafeVarargs
  public final InferMeta<T> chars(SerializableCharGetter<T>... getters) {
    return toNamed(getters, Meta::namedChar);
  }

  @SafeVarargs
  public final InferMeta<T> bytes(SerializableByteGetter<T>... getters) {
    return toNamed(getters, Meta::namedByte);
  }

  @SafeVarargs
  public final InferMeta<T> shorts(SerializableShortGetter<T>... getters) {
    return toNamed(getters, Meta::namedShort);
  }

  @SafeVarargs
  public final InferMeta<T> ints(SerializableIntGetter<T>... getters) {
    return toNamed(getters, Meta::namedInt);
  }

  @SafeVarargs
  public final InferMeta<T> longs(SerializableLongGetter<T>... getters) {
    return toNamed(getters, Meta::namedLong);
  }

  @SafeVarargs
  public final InferMeta<T> floats(SerializableFloatGetter<T>... getters) {
    return toNamed(getters, Meta::namedFloat);
  }

  @SafeVarargs
  public final InferMeta<T> doubles(SerializableDoubleGetter<T>... getters) {
    return toNamed(getters, Meta::namedDouble);
  }

  private <G extends Serializable> InferMeta<T> toNamed(G[] getters, TriFunction<Meta<T>, String, G, Meta<T>> transform) {
    Meta<T> meta = this.meta;
    for (G prop : getters) {
      String name = toName(prop);
      meta = transform.apply(meta, name, prop);
    }
    return from(meta);
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
