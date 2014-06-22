package uk.kludje.test;

import uk.kludje.Exceptions;

import java.util.function.Function;

public interface UFunction<T, R> extends Function<T, R> {

  default R apply(T t) {
    try {
      return applyChecked(t);
    } catch (Throwable e) {
      throw Exceptions.throwChecked(e);
    }
  }

  R applyChecked(T t) throws Throwable;
}
