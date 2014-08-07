package uk.kludje;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

/**
 * Provides common property-based object implementation methods.
 *
 * @param <T>
 */
public final class Meta<T> {
  private final Class<T> type;
  private final List<BiConsumer<T, StringBuilder>> toString;
  private final List<BiPredicate<T, T>> equals;
  private final List<ToIntFunction<T>> hashCode;

  private Meta(Class<T> type,
               List<BiConsumer<T, StringBuilder>> toString,
               List<BiPredicate<T, T>> equals,
               List<ToIntFunction<T>> hashCode) {
    this.type = type;
    this.toString = toString;
    this.equals = equals;
    this.hashCode = hashCode;
  }

  public static <T> Meta<T> meta(Class<T> type) {
    return new Meta<T>(type, Collections.emptyList(), Collections.emptyList(), Collections.emptyList());
  }

  public Class<T> getType() {
    return type;
  }

  public Meta<T> objects(Getter<T>... getters) {
    return update(getters,
        str()::objects,
        eq()::objects,
        hash()::objects);
  }

  public Meta<T> booleans(BooleanGetter<T>... getters) {
    return update(getters,
        str()::booleans,
        eq()::booleans,
        hash()::booleans);
  }

  public Meta<T> chars(CharGetter<T>... getters) {
    return update(getters,
        str()::chars,
        eq()::chars,
        hash()::chars);
  }

  public Meta<T> bytes(ByteGetter<T>... getters) {
    return update(getters,
        str()::bytes,
        eq()::bytes,
        hash()::bytes);
  }

  public Meta<T> shorts(ShortGetter<T>... getters) {
    return update(getters,
        str()::shorts,
        eq()::shorts,
        hash()::shorts);
  }

  public Meta<T> ints(IntGetter<T>... getters) {
    return update(getters,
        str()::ints,
        eq()::ints,
        hash()::ints);
  }

  public Meta<T> longs(LongGetter<T>... getters) {
    return update(getters,
        str()::longs,
        eq()::longs,
        hash()::longs);
  }

  public Meta<T> floats(FloatGetter<T>... getters) {
    return update(getters,
        str()::floats,
        eq()::floats,
        hash()::floats);
  }

  public Meta<T> doubles(DoubleGetter<T>... getters) {
    return update(getters,
        str()::doubles,
        eq()::doubles,
        hash()::doubles);
  }

  public <G> Meta<T> update(G[] getters,
                            Function<G, BiConsumer<T, StringBuilder>> strTransform,
                            Function<G, BiPredicate<T, T>> eqTransform,
                            Function<G, ToIntFunction<T>> hashTransform) {
    List<BiConsumer<T, StringBuilder>> newToString = combine(toString, getters, strTransform);
    List<BiPredicate<T, T>> newEquals = combine(equals, getters, eqTransform);
    List<ToIntFunction<T>> newHashCode = combine(hashCode, getters, hashTransform);
    return new Meta<T>(type, newToString, newEquals, newHashCode);
  }

  private <A, R> List<R> combine(List<R> existing, A[] src, Function<A, R> transform) {
    List<R> result = new ArrayList<>();
    List<R> transformed = Arrays.asList(src).stream()
        .map(transform)
        .collect(Collectors.toList());
    result.addAll(existing);
    result.addAll(transformed);
    return result;
  }

  public boolean equals(T t, Object any) {
    assert type.isInstance(t);
    if (any == t) {
      return true;
    }
    if (!type.isInstance(any)) {
      return false;
    }
    T other = (T) any;
    return equals.stream()
        .allMatch(p -> p.test(t, other));
  }

  public int hashCode(T t) {
    int result = 0;
    for (ToIntFunction<T> fn : hashCode) {
      result = (result * 13) + fn.applyAsInt(t);
    }
    return result;
  }

  public String toString(T t) {
    StringBuilder buf = new StringBuilder();
    buf.append(type.getSimpleName());
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
    return (ToStringTransformer<T>) ToStringTransformer.INST;
  }

  private EqualsTransformer<T> eq() {
    return (EqualsTransformer<T>) EqualsTransformer.INST;
  }

  private HashTransformer<T> hash() {
    return (HashTransformer<T>) HashTransformer.INST;
  }

  @FunctionalInterface
  public static interface Getter<T> {
    Object get(T t);
  }

  @FunctionalInterface
  public static interface BooleanGetter<T> {
    boolean get(T t);
  }

  @FunctionalInterface
  public static interface CharGetter<T> {
    char get(T t);
  }

  @FunctionalInterface
  public static interface ByteGetter<T> {
    byte get(T t);
  }

  @FunctionalInterface
  public static interface ShortGetter<T> {
    short get(T t);
  }

  @FunctionalInterface
  public static interface IntGetter<T> {
    int get(T t);
  }

  @FunctionalInterface
  public static interface LongGetter<T> {
    int get(T t);
  }

  @FunctionalInterface
  public static interface FloatGetter<T> {
    float get(T t);
  }

  @FunctionalInterface
  public static interface DoubleGetter<T> {
    double get(T t);
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
    return (t, buf) -> buf.append(g.get(t));
  }

  @Override
  public BiConsumer<T, StringBuilder> chars(Meta.CharGetter<T> g) {
    return (t, buf) -> buf.append(g.get(t));
  }

  @Override
  public BiConsumer<T, StringBuilder> bytes(Meta.ByteGetter<T> g) {
    return (t, buf) -> buf.append(g.get(t));
  }

  @Override
  public BiConsumer<T, StringBuilder> shorts(Meta.ShortGetter<T> g) {
    return (t, buf) -> buf.append(g.get(t));
  }

  @Override
  public BiConsumer<T, StringBuilder> ints(Meta.IntGetter<T> g) {
    return (t, buf) -> buf.append(g.get(t));
  }

  @Override
  public BiConsumer<T, StringBuilder> longs(Meta.LongGetter<T> g) {
    return (t, buf) -> buf.append(g.get(t));
  }

  @Override
  public BiConsumer<T, StringBuilder> floats(Meta.FloatGetter<T> g) {
    return (t, buf) -> buf.append(g.get(t));
  }

  @Override
  public BiConsumer<T, StringBuilder> doubles(Meta.DoubleGetter<T> g) {
    return (t, buf) -> buf.append(g.get(t));
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
    return (t1, t2) -> g.get(t1) == g.get(t2);
  }

  @Override
  public BiPredicate<T, T> chars(Meta.CharGetter<T> g) {
    return (t1, t2) -> g.get(t1) == g.get(t2);
  }

  @Override
  public BiPredicate<T, T> bytes(Meta.ByteGetter<T> g) {
    return (t1, t2) -> g.get(t1) == g.get(t2);
  }

  @Override
  public BiPredicate<T, T> shorts(Meta.ShortGetter<T> g) {
    return (t1, t2) -> g.get(t1) == g.get(t2);
  }

  @Override
  public BiPredicate<T, T> ints(Meta.IntGetter<T> g) {
    return (t1, t2) -> g.get(t1) == g.get(t2);
  }

  @Override
  public BiPredicate<T, T> longs(Meta.LongGetter<T> g) {
    return (t1, t2) -> g.get(t1) == g.get(t2);
  }

  @Override
  public BiPredicate<T, T> floats(Meta.FloatGetter<T> g) {
    return (t1, t2) -> g.get(t1) == g.get(t2);
  }

  @Override
  public BiPredicate<T, T> doubles(Meta.DoubleGetter<T> g) {
    return (t1, t2) -> g.get(t1) == g.get(t2);
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
    return t -> Boolean.hashCode(g.get(t));
  }

  @Override
  public ToIntFunction<T> chars(Meta.CharGetter<T> g) {
    return t -> Character.hashCode(g.get(t));
  }

  @Override
  public ToIntFunction<T> bytes(Meta.ByteGetter<T> g) {
    return t -> Byte.hashCode(g.get(t));
  }

  @Override
  public ToIntFunction<T> shorts(Meta.ShortGetter<T> g) {
    return t -> Short.hashCode(g.get(t));
  }

  @Override
  public ToIntFunction<T> ints(Meta.IntGetter<T> g) {
    return t -> Integer.hashCode(g.get(t));
  }

  @Override
  public ToIntFunction<T> longs(Meta.LongGetter<T> g) {
    return t -> Long.hashCode(g.get(t));
  }

  @Override
  public ToIntFunction<T> floats(Meta.FloatGetter<T> g) {
    return t -> Float.hashCode(g.get(t));
  }

  @Override
  public ToIntFunction<T> doubles(Meta.DoubleGetter<T> g) {
    return t -> Double.hashCode(g.get(t));
  }
}