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

package uk.kludje.experimental.collect.array;

import uk.kludje.experimental.collect.SparseArray;

import java.util.Objects;

/**
 * A type for defining class contracts for implementers of the {@link SparseArray} type.
 * <p>
 * This type can be used to implement the methods or just to verify their correctness.
 */
public final class SparseArrayContracts {

  private SparseArrayContracts() {
  }

  /**
   * The {@code Object.equals(Object)} contract for a {@link SparseArray}.
   * <p>
   * <pre>
   * if (sparseArray == o) {
   *   return true;
   * }
   * if (o == null) {
   *   return false;
   * }
   * SparseArray&lt;?> other = (SparseArray&lt;?>) o;
   * if (sparseArray.size() == other.size()) {
   *   return sparseArray.keyStream().allMatch(key -> Objects.equals(sparseArray.get(key), other.get(key)));
   * }
   * return false;
   * </pre>
   *
   * @param sparseArray the sparse array
   * @param o           another object or null
   * @return true if equal
   */
  public static boolean areEqual(SparseArray<?> sparseArray, Object o) {
    if (sparseArray == o) {
      return true;
    }
    if (o == null) {
      return false;
    }
    SparseArray<?> other = (SparseArray<?>) o;
    if (sparseArray.size() == other.size()) {
      return sparseArray.keyStream().allMatch(key -> Objects.equals(sparseArray.get(key), other.get(key)));
    }
    return false;
  }

  /**
   * The {@code Object.hashCode()} contract for a {@link SparseArray}.
   * <p>
   * <pre>
   * int hash = 0;
   * for (SparseArrayEntry<?> entry : sparseArrayEntries) {
   *   hash += entry.hashCode();
   * }
   * return hash;
   * </pre>
   *
   * @param sparseArrayEntries
   * @return
   */
  public static int hashCodeOf(Iterable<SparseArrayEntry<?>> sparseArrayEntries) {
    int hash = 0;
    for (SparseArrayEntry<?> entry : sparseArrayEntries) {
      hash += entry.hashCode();
    }
    return hash;
  }
}
