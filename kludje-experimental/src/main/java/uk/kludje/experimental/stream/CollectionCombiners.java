package uk.kludje.experimental.stream;

import java.util.Collection;

public final class CollectionCombiners {

  private CollectionCombiners() {}

  /**
   * A combiner for mutable {@link Collection} types.
   * Usage: {@code BinaryOperator<List<String>> combiner = CollectionCombiners::combineMutable; }
   *
   * @param target the instance to call addAll on
   * @param source the argument for addAll
   * @param <E> the collection element type
   * @param <C> the collection type
   * @return the target
   */
  public static <E, C extends Collection<E>> C combineMutable(C target, C source) {
    target.addAll(source);
    return target;
  }
}
