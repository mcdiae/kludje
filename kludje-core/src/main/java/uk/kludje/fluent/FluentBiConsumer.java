package uk.kludje.fluent;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;

/**
 * Use to make any two-argument function fluent:
 * <pre>
 *   Map&lt;String, Integer&gt; map = new HashMap&lt;&gt;();
 *   FluentConsumer.fluent(map::put)
 *                 .apply("one", 1)
 *                 .apply("two", 2)
 *                 .apply("three", 3);
 * </pre>
 *
 * @param <T>
 */
public final class FluentBiConsumer<T, U> implements BiFunction<T, U, FluentBiConsumer<T, U>> {
  private final BiConsumer<T, U> consumer;

  private FluentBiConsumer(BiConsumer<T, U> consumer) {
    this.consumer = consumer;
  }

  public static <T, U> FluentBiConsumer<T, U> fluent(BiConsumer<T, U> consumer) {
    return new FluentBiConsumer<>(consumer);
  }

  @Override
  public FluentBiConsumer<T, U> apply(T t, U u) {
    consumer.accept(t, u);
    return this;
  }
}
