package uk.kludje.experimental.memoize;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class Memoization {
  public static <T, R> Function<T, R> memoize(Function<T, R> fn) {
    Map<T, R> store = new HashMap<>();

    return t ->  store.computeIfAbsent(t, fn);
  }
}
