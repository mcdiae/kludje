package uk.kludje.experimental.collect;

import uk.kludje.Ensure;
import uk.kludje.Nullifier;

import java.util.Iterator;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 *
 */
public final class CollectionNullifiers {

  private CollectionNullifiers() {}

  /**
   *
   *
   * @param <E>
   * @param <I> a collection of elements
   * @return
   */
  public static <E, I extends Iterable<E>> Nullifier<I, E> first() {
    return CollectionNullifiers::firstElement;
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

  public static <R, M extends Map<?, R>> Nullifier<M, R> keyed(Object key) {
    Ensure.that(key != null, "key != null");

    return map -> fromMapKey(map, key);
  }

  private static <R, M extends Map<?, R>> R fromMapKey(M map, Object key) {
    if (map == null) {
      return null;
    }

    return map.get(key);
  }
}
