/*
 *
 * Copyright $year McDowell
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
 * /
 */

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
