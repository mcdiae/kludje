package uk.kludje.test;

public interface Inherit<T> {
  T t(T t);

  public static interface Sub<E> extends Inherit<E> {

  }
}
