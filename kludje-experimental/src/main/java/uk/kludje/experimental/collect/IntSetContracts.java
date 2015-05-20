/*
 * Copyright 2015 McDowell
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

package uk.kludje.experimental.collect;

import java.util.Objects;
import java.util.StringJoiner;

/**
 * Convenience methods for implementing the {@link IntSet} contract.
 */
public final class IntSetContracts {
  private IntSetContracts() {
  }

  /**
   * Use to implement {@link IntSet#equals(Object)}.
   *
   * @param intSet
   * @param other
   * @return
   */
  public static boolean areEqual(IntSet intSet, Object other) {
    assert intSet != null;

    if (intSet == other) {
      return true;
    }

    if (other == null) {
      return false;
    }

    if (other instanceof IntSet) {
      IntSet sa = (IntSet) other;

      int size = intSet.size();

      if (size != sa.size()) {
        return false;
      }

      return intSet.stream()
          .allMatch(sa::contains);
    }

    return false;
  }

  /**
   * Use to implement {@link IntSet#hashCode()}.
   *
   * @param intSet
   * @return the hashCode
   */
  public static int hashCodeOf(IntSet intSet) {
    return intSet.size();
  }

  /**
   * May be used to implement {@link IntSet#toString()}.
   *
   * @param sparseArray
   * @return an informal debug string
   */
  public static String toString(IntSet sparseArray) {
    StringJoiner joiner = new StringJoiner(", ", "[", "]");

    for (int i = 0, size = sparseArray.size(); i < size; i++) {
      Object o = sparseArray.intAt(i);
      joiner.add(Objects.toString(o));
    }

    return joiner.toString();
  }
}
