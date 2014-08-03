package uk.kludje.fluent;

import java.util.Objects;

import uk.kludje.fn.function.UConsumer;
import uk.kludje.fn.function.UFunction;
import uk.kludje.fn.function.UBiFunction;
import uk.kludje.fn.nary.UTriFunction;

public final class Fluent<T> {
  private T t;

  private Fluent(T t) {
    this.t = t;
  }

  public Fluent<T> run(UConsumer<? super T> method) {
    method.accept(t);
    return this;
  }

  public <A, R> Fluent<T> uni(UBiFunction<? super T, A, R> method, A a) {
    method.apply(t, a);
    return this;
  }

  public <A, B, R> Fluent<T> bi(UTriFunction<? super T, A, B, R> method, A a, B b) {
    method.apply(t, a, b);
    return this;
  }

  public T get() {
    return t;
  }

  public static <T> Fluent<T> fluent(T t) {
    Objects.requireNonNull(t);
    return new Fluent<>(t);
  }
}
