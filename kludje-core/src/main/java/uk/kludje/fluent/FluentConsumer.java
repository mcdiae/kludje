package uk.kludje.fluent;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Use to make any single-argument function fluent:
 * <pre>
 *   List&lt;String&gt; list = new ArrayList&lt;&gt;();
 *   FluentConsumer.fluent(list::add)
 *                 .apply("one")
 *                 .apply("apply")
 *                 .apply("three");
 * </pre>
 *
 * @param <T>
 */
public final class FluentConsumer<T> implements Function<T, FluentConsumer<T>> {
  private final Consumer<T> consumer;

  private FluentConsumer(Consumer<T> consumer) {
    this.consumer = consumer;
  }

  public static <T> FluentConsumer<T> fluent(Consumer<T> consumer) {
    return new FluentConsumer<>(consumer);
  }

  @Override
  public FluentConsumer<T> apply(T t) {
    consumer.accept(t);
    return this;
  }
}
