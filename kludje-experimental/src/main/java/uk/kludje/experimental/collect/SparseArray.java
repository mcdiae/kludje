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

import java.util.Collection;

/**
 * A simple sparse array interface.
 *
 * @param <V> the value type
 */
public interface SparseArray<V> {

  V get(int key);

  int keyAt(int index);

  int size();

  IntSequence keys();

  Collection<V> values();

  /**
   * Places a key-value pair into the collection.
   * The default implementation throws a {@link UnsupportedOperationException}.
   *
   * @param key   the key
   * @param value the value
   * @return the old value or null if none
   */
  default V put(int key, V value) {
    throw new UnsupportedOperationException();
  }

  /**
   * Removes a key-value pair from the collection.
   * The default implementation throws a {@link UnsupportedOperationException}.
   *
   * @param key the key
   * @return the removed value or null if none
   */
  default V remove(int key) {
    throw new UnsupportedOperationException();
  }

  /**
   * Calls {@link #put(int, Object)}.
   *
   * @param key   the key
   * @param value the value
   * @return this
   */
  default SparseArray<V> with(int key, V value) {
    put(key, value);
    return this;
  }

  /**
   * @return a wrapper without the mutable methods
   */
  default SparseArray<V> asUnmodifiable() {
    SparseArray<V> sa = this;

    class Unmodifiable extends AbstractSparseArray<V> {

      @Override
      public V get(int key) {
        return sa.get(key);
      }

      @Override
      public int keyAt(int index) {
        return sa.keyAt(index);
      }

      @Override
      public int size() {
        return sa.size();
      }

      @Override
      public IntSequence keys() {
        return sa.keys();
      }

      @Override
      public Collection<V> values() {
        return sa.values();
      }
    }

    return new Unmodifiable();
  }

  /**
   * @param obj the equality candidate
   * @return true if the object is considered equal
   * @see SparseArrayContracts#areEqual(SparseArray, Object)
   */
  @Override
  boolean equals(Object obj);

  /**
   * @return the hash
   * @see SparseArrayContracts#hashCodeOf(SparseArray)
   */
  @Override
  int hashCode();

  /**
   * The string form of a {@code SparseArray} is not considered sufficient for reproducing the collection.
   *
   * @return a string form for debugging purposes
   * @see SparseArrayContracts#toString(SparseArray)
   */
  @Override
  String toString();
}

