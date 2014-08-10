/*
 * Copyright 2014 McDowell
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

import java.util.*;
import static java.util.Collections.emptyList;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;
import static java.util.Arrays.asList;

/**
 * <p>Provides a basic meta-method builder for common {@code Object} method implementations.</p>
 *
 * <p>Example that builds equals, hashCode and toString methods using the id, name and dateOfBirth properties:</p>
 *
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
 *
 * <p>Note: arrays are treated as objects; use a decorator to provide alternative equals/hashCode/toString behaviour.
 * For example, Google Guava's {@code Bytes.asList(byte...)}.</p>
 *
 * <p>Instances of this type are immutable and thread safe.</p>
 *
 * @param <T> the accessed type
 */
public final class Meta<T> {
  private static final Meta<?> INIT = new Meta<>(emptyList(), emptyList(), emptyList());

  private final List<BiConsumer<T, StringBuilder>> toString;
  private final List<BiPredicate<T, T>> equals;
  private final List<ToIntFunction<T>> hashCode;

  private Meta(List<BiConsumer<T, StringBuilder>> toString,
               List<BiPredicate<T, T>> equals,
               List<ToIntFunction<T>> hashCode) {
    this.toString = toString;
    this.equals = equals;
    this.hashCode = hashCode;
  }

  /**
   * @param <T> the type of class
   * @return a new instance
   */
  public static <T> Meta<T> meta() {
    @SuppressWarnings("unchecked")
    Meta<T> safe = (Meta<T>) INIT;
    return safe;
  }

  /**
   * Use to specify properties of type object that should be considered by this type.
   *
   * Do not use this method for primitive properties - alternatives have been provided.
   *
   * This method does not mutate the instance; it returns a new one.
   *
   * @param getters a vararg array of non-null getters
   * @return a new instance
   */
  @SafeVarargs
  public final Meta<T> objects(Getter<T>... getters) {
    @SuppressWarnings("varargs")
    List<Getter<T>> list = asList(getters);
    return update(list,
        str()::objects,
        eq()::objects,
        hash()::objects);
  }

  @SafeVarargs
  public final Meta<T> booleans(BooleanGetter<T>... getters) {
    @SuppressWarnings("varargs")
    List<BooleanGetter<T>> list = asList(getters);
    return update(list,
        str()::booleans,
        eq()::booleans,
        hash()::booleans);
  }

  @SafeVarargs
  public final Meta<T> chars(CharGetter<T>... getters) {
    @SuppressWarnings("varargs")
    List<CharGetter<T>> list = asList(getters);
    return update(list,
        str()::chars,
        eq()::chars,
        hash()::chars);
  }

  @SafeVarargs
  public final Meta<T> bytes(ByteGetter<T>... getters) {
    @SuppressWarnings("varargs")
    List<ByteGetter<T>> list = asList(getters);
    return update(list,
        str()::bytes,
        eq()::bytes,
        hash()::bytes);
  }

  @SafeVarargs
  public final Meta<T> shorts(ShortGetter<T>... getters) {
    @SuppressWarnings("varargs")
    List<ShortGetter<T>> list = asList(getters);
    return update(list,
        str()::shorts,
        eq()::shorts,
        hash()::shorts);
  }

  @SafeVarargs
  public final Meta<T> ints(IntGetter<T>... getters) {
    @SuppressWarnings("varargs")
    List<IntGetter<T>> list = asList(getters);
    return update(list,
        str()::ints,
        eq()::ints,
        hash()::ints);
  }

  @SafeVarargs
  public final Meta<T> longs(LongGetter<T>... getters) {
    @SuppressWarnings("varargs")
    List<LongGetter<T>> list = asList(getters);
    return update(list,
        str()::longs,
        eq()::longs,
        hash()::longs);
  }

  @SafeVarargs
  public final Meta<T> floats(FloatGetter<T>... getters) {
    @SuppressWarnings("varargs")
    List<FloatGetter<T>> list = asList(getters);
    return update(list,
        str()::floats,
        eq()::floats,
        hash()::floats);
  }

  @SafeVarargs
  public final Meta<T> doubles(DoubleGetter<T>... getters) {
    @SuppressWarnings("varargs")
    List<DoubleGetter<T>> list = asList(getters);
    return update(list,
        str()::doubles,
        eq()::doubles,
        hash()::doubles);
  }

  private <G> Meta<T> update(List<G> getters,
                            Function<G, BiConsumer<T, StringBuilder>> strTransform,
                            Function<G, BiPredicate<T, T>> eqTransform,
                            Function<G, ToIntFunction<T>> hashTransform) {
    List<BiConsumer<T, StringBuilder>> newToString = combine(toString, getters, strTransform);
    List<BiPredicate<T, T>> newEquals = combine(equals, getters, eqTransform);
    List<ToIntFunction<T>> newHashCode = combine(hashCode, getters, hashTransform);
    return new Meta<>(newToString, newEquals, newHashCode);
  }

  private <A, R> List<R> combine(List<R> existing, List<A> src, Function<A, R> transform) {
    List<R> result = new ArrayList<>();
    List<R> transformed = src.stream()
        .map(transform)
        .collect(Collectors.toList());
    result.addAll(existing);
    result.addAll(transformed);
    return result;
  }

  /**
   * {@code any} is equal to {@code t} if it is of type {@code T}
   * and all the properties defined by this type are equal.
   *
   * @param t a non-null instance of type T
   * @param any any object, including null
   * @return true if the arguments are equal
   */
  public boolean equals(T t, Object any) {
    if (!t.getClass().isInstance(any)) {
      return false;
    }
    if (any == t) {
      return true;
    }
    @SuppressWarnings("unchecked")
    T other = (T) any;
    return equals.stream()
        .allMatch(p -> p.test(t, other));
  }

  /**
   * Creates a hash of all values defined by the properties provided to this instance.
   * The hashing formula is not specified.
   *
   * @param t the non-null instance to create a hash for
   * @return the hash
   */
  public int hashCode(T t) {
    int result = 0;
    for (ToIntFunction<T> fn : hashCode) {
      result = (result * 31) + fn.applyAsInt(t);
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
  public String toString(T t) {
    StringBuilder buf = new StringBuilder();
    buf.append(t.getClass().getSimpleName());
    buf.append(" {");
    for (int i = 0; i < toString.size(); i++) {
      if (i > 0) {
        buf.append(", ");
      }
      toString.get(i).accept(t, buf);
    }
    return buf.append("}").toString();
  }

  private ToStringTransformer<T> str() {
    @SuppressWarnings("unchecked")
    ToStringTransformer<T> typed = (ToStringTransformer<T>) ToStringTransformer.INST;
    return typed;
  }

  private EqualsTransformer<T> eq() {
    @SuppressWarnings("unchecked")
    EqualsTransformer<T> typed = (EqualsTransformer<T>) EqualsTransformer.INST;
    return typed;
  }

  private HashTransformer<T> hash() {
    @SuppressWarnings("unchecked")
    HashTransformer<T> typed = (HashTransformer<T>) HashTransformer.INST;
    return typed;
  }

  /**
   * A functional interface for reading a property value.
   * Alternative types have been provided for primitives.
   *
   * @param <T> the type to read the property from
   * @see Meta#objects(uk.kludje.Meta.Getter[])
   */
  @FunctionalInterface
  public static interface Getter<T> {
    /**
     * Reads a property value from the argument.
     *
     * @param t the source of the property value
     * @return the property value instance or null
     */
    Object get(T t);
  }

  @FunctionalInterface
  public static interface BooleanGetter<T> {
    boolean getBoolean(T t);
  }

  @FunctionalInterface
  public static interface CharGetter<T> {
    char getChar(T t);
  }

  @FunctionalInterface
  public static interface ByteGetter<T> {
    byte getByte(T t);
  }

  @FunctionalInterface
  public static interface ShortGetter<T> {
    short getShort(T t);
  }

  @FunctionalInterface
  public static interface IntGetter<T> {
    int getInt(T t);
  }

  @FunctionalInterface
  public static interface LongGetter<T> {
    long getLong(T t);
  }

  @FunctionalInterface
  public static interface FloatGetter<T> {
    float getFloat(T t);
  }

  @FunctionalInterface
  public static interface DoubleGetter<T> {
    double getDouble(T t);
  }
}

interface GetterTransformer<T, U> {
  U objects(Meta.Getter<T> g);

  U booleans(Meta.BooleanGetter<T> g);

  U chars(Meta.CharGetter<T> g);

  U bytes(Meta.ByteGetter<T> g);

  U shorts(Meta.ShortGetter<T> g);

  U ints(Meta.IntGetter<T> g);

  U longs(Meta.LongGetter<T> g);

  U floats(Meta.FloatGetter<T> g);

  U doubles(Meta.DoubleGetter<T> g);
}

class ToStringTransformer<T> implements GetterTransformer<T, BiConsumer<T, StringBuilder>> {
  public static final ToStringTransformer<?> INST = new ToStringTransformer<>();

  private ToStringTransformer() {
  }

  @Override
  public BiConsumer<T, StringBuilder> objects(Meta.Getter<T> g) {
    return (t, buf) -> buf.append(g.get(t));
  }

  @Override
  public BiConsumer<T, StringBuilder> booleans(Meta.BooleanGetter<T> g) {
    return (t, buf) -> buf.append(g.getBoolean(t));
  }

  @Override
  public BiConsumer<T, StringBuilder> chars(Meta.CharGetter<T> g) {
    return (t, buf) -> buf.append(g.getChar(t));
  }

  @Override
  public BiConsumer<T, StringBuilder> bytes(Meta.ByteGetter<T> g) {
    return (t, buf) -> buf.append(g.getByte(t));
  }

  @Override
  public BiConsumer<T, StringBuilder> shorts(Meta.ShortGetter<T> g) {
    return (t, buf) -> buf.append(g.getShort(t));
  }

  @Override
  public BiConsumer<T, StringBuilder> ints(Meta.IntGetter<T> g) {
    return (t, buf) -> buf.append(g.getInt(t));
  }

  @Override
  public BiConsumer<T, StringBuilder> longs(Meta.LongGetter<T> g) {
    return (t, buf) -> buf.append(g.getLong(t));
  }

  @Override
  public BiConsumer<T, StringBuilder> floats(Meta.FloatGetter<T> g) {
    return (t, buf) -> buf.append(g.getFloat(t));
  }

  @Override
  public BiConsumer<T, StringBuilder> doubles(Meta.DoubleGetter<T> g) {
    return (t, buf) -> buf.append(g.getDouble(t));
  }
}

class EqualsTransformer<T> implements GetterTransformer<T, BiPredicate<T, T>> {
  public static final EqualsTransformer<?> INST = new EqualsTransformer<>();

  private EqualsTransformer() {
  }

  @Override
  public BiPredicate<T, T> objects(Meta.Getter<T> g) {
    return (t1, t2) -> Objects.equals(g.get(t1), g.get(t2));
  }

  @Override
  public BiPredicate<T, T> booleans(Meta.BooleanGetter<T> g) {
    return (t1, t2) -> g.getBoolean(t1) == g.getBoolean(t2);
  }

  @Override
  public BiPredicate<T, T> chars(Meta.CharGetter<T> g) {
    return (t1, t2) -> g.getChar(t1) == g.getChar(t2);
  }

  @Override
  public BiPredicate<T, T> bytes(Meta.ByteGetter<T> g) {
    return (t1, t2) -> g.getByte(t1) == g.getByte(t2);
  }

  @Override
  public BiPredicate<T, T> shorts(Meta.ShortGetter<T> g) {
    return (t1, t2) -> g.getShort(t1) == g.getShort(t2);
  }

  @Override
  public BiPredicate<T, T> ints(Meta.IntGetter<T> g) {
    return (t1, t2) -> g.getInt(t1) == g.getInt(t2);
  }

  @Override
  public BiPredicate<T, T> longs(Meta.LongGetter<T> g) {
    return (t1, t2) -> g.getLong(t1) == g.getLong(t2);
  }

  @Override
  public BiPredicate<T, T> floats(Meta.FloatGetter<T> g) {
    return (t1, t2) -> Float.compare(g.getFloat(t1), g.getFloat(t2)) == 0;
  }

  @Override
  public BiPredicate<T, T> doubles(Meta.DoubleGetter<T> g) {
    return (t1, t2) -> Double.compare(g.getDouble(t1), g.getDouble(t2)) == 0;
  }
}

class HashTransformer<T> implements GetterTransformer<T, ToIntFunction<T>> {
  public static final HashTransformer<?> INST = new HashTransformer<>();

  private HashTransformer() {
  }

  @Override
  public ToIntFunction<T> objects(Meta.Getter<T> g) {
    return t -> Objects.hashCode(g.get(t));
  }

  @Override
  public ToIntFunction<T> booleans(Meta.BooleanGetter<T> g) {
    return t -> Boolean.hashCode(g.getBoolean(t));
  }

  @Override
  public ToIntFunction<T> chars(Meta.CharGetter<T> g) {
    return t -> Character.hashCode(g.getChar(t));
  }

  @Override
  public ToIntFunction<T> bytes(Meta.ByteGetter<T> g) {
    return t -> Byte.hashCode(g.getByte(t));
  }

  @Override
  public ToIntFunction<T> shorts(Meta.ShortGetter<T> g) {
    return t -> Short.hashCode(g.getShort(t));
  }

  @Override
  public ToIntFunction<T> ints(Meta.IntGetter<T> g) {
    return t -> Integer.hashCode(g.getInt(t));
  }

  @Override
  public ToIntFunction<T> longs(Meta.LongGetter<T> g) {
    return t -> Long.hashCode(g.getLong(t));
  }

  @Override
  public ToIntFunction<T> floats(Meta.FloatGetter<T> g) {
    return t -> Float.hashCode(g.getFloat(t));
  }

  @Override
  public ToIntFunction<T> doubles(Meta.DoubleGetter<T> g) {
    return t -> Double.hashCode(g.getDouble(t));
  }
}