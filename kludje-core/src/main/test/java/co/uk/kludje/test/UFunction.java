package java.co.uk.kludje.test;

import co.uk.kludje.Exceptions;

import java.util.function.Function;

public interface UFunction<T, R> extends Function<T, R> {

  default R apply(T t) {
    try {
      return applyChecked(t);
    } catch (Throwable e) {
      Exceptions.throwChecked(e);
      return null;
    }
  }

  R applyChecked(T t) throws Throwable;
}
