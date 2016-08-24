package uk.kludje.experimental;

import uk.kludje.Ensure;
import uk.kludje.Nullifier;

import java.util.Iterator;
import java.util.function.BiFunction;
import java.util.function.Function;

public final class Nullifiers {

  private Nullifiers() {}

  public static <E, I extends Iterable<E>> Nullifier<I, E> first() {
    return Nullifiers::firstElement;
  }

  private static <E, I extends Iterable<E>> E firstElement(I source) {
    if (source == null) {
      return null;
    }

    Iterator<E> iterator = source.iterator();
    Ensure.that(iterator != null, "iterator != null");

    return iterator.hasNext()
      ? iterator.next()
      : null;
  }

  public static <T, R> Nullifier<T, R> map(Function<T, R> fn) {
    Ensure.that(fn != null, "fn != null");

    return (t) -> mapTo(fn, t);
  }

  private static <T, R> R mapTo(Function<T, R> fn, T t) {
    if (t == null) {
      return null;
    }

    return fn.apply(t);
  }

  public static <T, U, R> Nullifier<T, R> biMap(BiFunction<T, U, R> fn, U u) {
    Ensure.that(fn != null, "fn != null");

    return (t) -> mapBiTo(fn, t, u);
  }

  private static <T, U, R> R mapBiTo(BiFunction<T, U, R> fn, T t, U u) {
    if (t == null) {
      return null;
    }

    return fn.apply(t, u);
  }
}
