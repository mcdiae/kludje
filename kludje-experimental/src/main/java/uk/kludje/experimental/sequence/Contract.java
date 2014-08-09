package uk.kludje.experimental.sequence;

import java.util.function.IntPredicate;
import java.util.function.IntUnaryOperator;

final class Contract {
  private Contract() {
  }

  public static int hashOf(Sequence<?> seq, IntUnaryOperator fn) {
    return seq.indices().reduce(0, (hash, idx) -> {
      return hash * 31 + fn.applyAsInt(idx);
    });
  }

  public static <T> boolean areEqual(Sequence<T> s1, Sequence<T> s2, IntPredicate ip) {
    int len = s1.length();
    if (len != s2.length()) {
      return false;
    }
    for (int i = 0; i < len; i++) {
      if (!ip.test(i)) {
        return false;
      }
    }
    return true;
  }

  public static String string(Sequence<?> seq, IndexPrinter consumer) {
    StringBuilder buf = new StringBuilder();
    seq.indices().forEach(i -> consumer.print(i, buf));
    return buf.toString();
  }

  public static interface IndexPrinter {
    void print(int index, StringBuilder buf);
  }
}
