package uk.kludje.test;

public interface ObjectMethods {
  String toString();

  boolean equals(Object o);

  int hashCode();

  Object clone();

  void finalize();

  void foo();
}
