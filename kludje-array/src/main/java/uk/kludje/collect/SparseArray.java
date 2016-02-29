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

package uk.kludje.collect;

import uk.kludje.collect.array.SparseArrayContracts;
import uk.kludje.collect.array.SparseArrayEntry;

import java.util.stream.IntStream;

/**
 * Interface that defines a non-contiguous array.
 */
public interface SparseArray<V> extends Iterable<SparseArrayEntry<V>> {

  /**
   * @param key the index to get
   * @return the value at that index or null
   */
  V get(int key);

  /**
   * @return the number of entries
   */
  int size();

  /**
   * @param key the index to test
   * @return true if an entry exists at that index
   */
  boolean contains(int key);

  /**
   * @return a stream of the given keys
   */
  IntStream keyStream();

  /**
   * @return true if size() == 0
   */
  default boolean isEmpty() {
    return size() == 0;
  }

  /**
   * @return false if size() == 0
   */
  default boolean isNotEmpty() {
    return size() != 0;
  }

  /**
   * See {@link SparseArrayContracts#areEqual(SparseArray, SparseArray)} for the equality contract.
   *
   * @param o the object to compare to
   * @return true if o is the same
   */
  boolean equals(Object o);

  /**
   * See {@link SparseArrayContracts#hashCodeOf(Iterable)} for the hash contract.
   *
   * @return the hash code
   */
  int hashCode();
}
