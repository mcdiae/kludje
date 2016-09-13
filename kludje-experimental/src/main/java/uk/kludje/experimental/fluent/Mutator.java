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

package uk.kludje.experimental.fluent;

import uk.kludje.fn.nary.TetraConsumer;
import uk.kludje.fn.nary.TriConsumer;
import uk.kludje.fn.nary.TriFunction;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Type for mutating an object using method references.
 *
 * @param <T> the type of the underlying value
 */
public final class Mutator<T> {
  private final T t;

  private Mutator(T t) {
    this.t = t;
  }

  /**
   * Creates a new mutate instance.
   *
   * @param t   the non-null type to adapt
   * @param <T> the adapted type
   * @return a new instance
   */
  public static <T> Mutator<T> mutate(T t) {
    Objects.requireNonNull(t);
    return new Mutator<>(t);
  }

  /**
   * Passes the underlying value to a consumer.
   * Usage:
   * <pre>
   *   AtomicInteger two = mutate(new AtomicInteger())
   *   .nil(AtomicInteger::incrementAndGet)
   *   .nil(AtomicInteger::incrementAndGet)
   *   .get();
   * </pre>
   *
   * @param consumer typically a method reference for T
   * @return this
   */
  public Mutator<T> nil(Consumer<? super T> consumer) {
    consumer.accept(t);
    return this;
  }

  /**
   * Passes the underlying value to a consumer, with an argument.
   * Usage:
   * <pre>
   * List&lt;String&gt; list = mutate(new ArrayList&lt;String&gt;())
   *                           .nil(List::add, "a")
   *                           .nil(List::add, "b")
   *                           .nil(List::add, "c")
   *                           .nil(List::remove, "b")
   *                           .map(Collections::unmodifiableList)
   *                           .get();
   * </pre>
   *
   * @param consumer typically a method reference for T
   * @param a        an argument
   * @param <A>      argument type
   * @return this
   */
  public <A> Mutator<T> un(BiConsumer<? super T, A> consumer, A a) {
    consumer.accept(t, a);
    return this;
  }

  /**
   * Passes the underlying value to a consumer, with two arguments.
   * Usage:
   * <pre>
   *   Map&lt;String, String&gt; map = mutate(new HashMap&lt;String, String&gt;())
   *                                  .nil(Map::put, "a", "A")
   *                                  .nil(Map::put, "b", "B")
   *                                  .nil(Map::put, "c", "C")
   *                                  .map(Collections::unmodifiableMap)
   *                                  .get();
   * </pre>
   *
   * @param consumer typically a method reference for T
   * @param a        first argument
   * @param b        second argument
   * @param <A>      type of a
   * @param <B>      type of b
   * @return this
   */
  public <A, B> Mutator<T> bi(TriConsumer<? super T, A, B> consumer, A a, B b) {
    consumer.accept(t, a, b);
    return this;
  }

  public <A, B, C> Mutator<T> tri(TetraConsumer<? super T, A, B, C> consumer, A a, B b, C c) {
    consumer.accept(t, a, b, c);
    return this;
  }

  public Nullary<T> nullary(Consumer<? super T> consumer) {
    Objects.requireNonNull(consumer);
    return new Nullary<>(this, consumer);
  }

  public <A> Unary<T, A> unary(BiConsumer<? super T, A> consumer) {
    Objects.requireNonNull(consumer);
    return new Unary<>(this, consumer);
  }

  public <A, B> Binary<T, A, B> binary(TriConsumer<? super T, A, B> consumer) {
    Objects.requireNonNull(consumer);
    return new Binary<>(this, consumer);
  }

  public <A, B, C> Trinary<T, A, B, C> binary(TetraConsumer<? super T, A, B, C> consumer) {
    Objects.requireNonNull(consumer);
    return new Trinary<>(this, consumer);
  }

  /**
   * Unwraps the value.
   *
   * @return the underlying value
   */
  public T get() {
    return t;
  }

  public <M> Mutator<M> map(Function<T, M> mapper) {
    return new Mutator<M>(mapper.apply(t));
  }

  public <M, A> Mutator<M> map(BiFunction<T, A, M> mapper, A a) {
    return new Mutator<M>(mapper.apply(t, a));
  }

  public <M, A, B> Mutator<M> map(TriFunction<T, A, B, M> mapper, A a, B b) {
    return new Mutator<M>(mapper.apply(t, a, b));
  }

  /**
   * A string of undefined form for debugging purposes.
   *
   * @return a string form.
   */
  @Override
  public String toString() {
    return "Fluent{" + t + "}";
  }

  public static abstract class FluentMethod<T> {
    private final Mutator<T> parent;

    private FluentMethod(Mutator<T> parent) {
      this.parent = parent;
    }

    public Mutator<T> unbind() {
      return parent;
    }

    public T get() {
      return parent.t;
    }
  }

  public static class Nullary<T> extends FluentMethod<T> {
    private final Consumer<? super T> consumer;

    private Nullary(Mutator<T> parent, Consumer<? super T> consumer) {
      super(parent);
      this.consumer = consumer;
    }

    public Nullary<T> invoke() {
      consumer.accept(get());
      return this;
    }
  }

  public static class Unary<T, A> extends FluentMethod<T> {
    private final BiConsumer<? super T, A> consumer;

    private Unary(Mutator<T> parent, BiConsumer<? super T, A> consumer) {
      super(parent);
      this.consumer = consumer;
    }

    public Unary<T, A> invoke(A a) {
      consumer.accept(get(), a);
      return this;
    }
  }

  public static class Binary<T, A, B> extends FluentMethod<T> {
    private final TriConsumer<? super T, A, B> consumer;

    private Binary(Mutator<T> parent, TriConsumer<? super T, A, B> consumer) {
      super(parent);
      this.consumer = consumer;
    }

    public Binary<T, A, B> invoke(A a, B b) {
      consumer.accept(get(), a, b);
      return this;
    }
  }

  public static class Trinary<T, A, B, C> extends FluentMethod<T> {
    private final TetraConsumer<? super T, A, B, C> consumer;

    private Trinary(Mutator<T> parent, TetraConsumer<? super T, A, B, C> consumer) {
      super(parent);
      this.consumer = consumer;
    }

    public Trinary<T, A, B, C> invoke(A a, B b, C c) {
      consumer.accept(get(), a, b, c);
      return this;
    }
  }
}
