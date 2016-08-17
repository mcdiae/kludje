package uk.kludje.experimental.stream;

import uk.kludje.Ensure;

import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;

public final class Combiners {

  private Combiners() {}

  /**
   * <p>The intent is to use the given consumer to mutate the left argument and then return it.</p>
   * <p>
   * Example: {@code BinaryOperator<List<String>> combiner = Combiners.mutate(List::addAll); }
   * </p>
   * <p>
   *   The result is equivalent to {@code BinaryOperator<List<String>> combiner = (left, right) -> { left.addAll(right); return left; } }
   * </p>
   *
   * @param mutator mutates the left argument to add data from the right argument
   * @param <T> the container type
   * @return a binary operator
   * @see java.util.stream.Collector#combiner()
   */
  public static <T> BinaryOperator<T> mutate(BiConsumer<T, T> mutator) {
    Ensure.that(mutator != null, "mutator != null");

    return (left, right) -> consumeAndReturn(mutator, left, right);
  }

  private static <T> T consumeAndReturn(BiConsumer<T, T> mutator, T left, T right) {
    mutator.accept(left, right);
    return left;
  }
}
