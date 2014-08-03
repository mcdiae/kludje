package uk.kludje.fluent;

import uk.kludje.fn.nary.TriConsumer;
import uk.kludje.fn.nary.TriFunction;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Makes any type fluent.
 *
 * @param <T>
 */
public final class Fluent<T> {
  private T t;

  private Fluent(T t) {
    this.t = t;
  }

  public static <T> Fluent<T> fluent(T t) {
    Objects.requireNonNull(t);
    return new Fluent<>(t);
  }

  public Fluent<T> f(Consumer<? super T> method) {
    method.accept(t);
    return this;
  }

  public <A> Fluent<T> f(BiConsumer<? super T, A> method, A a) {
    method.accept(t, a);
    return this;
  }

  public <A, B> Fluent<T> f(TriConsumer<? super T, A, B> method, A a, B b) {
    method.accept(t, a, b);
    return this;
  }

  public T get() {
    return t;
  }

  public <M> Fluent<M> map(Function<T, M> mapper) {
    return new Fluent<>(mapper.apply(t));
  }

  public <M, A> Fluent<M> map(BiFunction<T, A, M> mapper, A a) {
    return new Fluent<>(mapper.apply(t, a));
  }

  public <M, A, B> Fluent<M> map(TriFunction<T, A, B, M> mapper, A a, B b) {
    return new Fluent<>(mapper.apply(t, a, b));
  }
}
