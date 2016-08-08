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

import uk.kludje.experimental.array.LinearSearch;
import uk.kludje.experimental.collect.SparseArray;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.IntStream;

final class ArrayBackedSparseArray<V> implements SparseArray<V> {

  private final int[] keys;
  private final Object[] values;

  private ArrayBackedSparseArray(int[] keys, Object[] values) {
    this.keys = keys;
    this.values = values;

    assert keys.length == values.length : "keys.length == values.length";
  }

  @Override
  public V get(int key) {
    int index = LinearSearch.find(keys, 0, keys.length, key);
    Object element = (index == LinearSearch.NOT_FOUND) ? null : values[index];
    @SuppressWarnings("unchecked")
    V value = (V) element;
    return value;
  }

  @Override
  public int size() {
    return keys.length;
  }

  @Override
  public boolean contains(int key) {
    return LinearSearch.find(keys, 0, keys.length, key) != LinearSearch.NOT_FOUND;
  }

  @Override
  public IntStream keyStream() {
    return IntStream.of(keys);
  }

  @Override
  public Iterator<SparseArrayEntry<V>> iterator() {
    class SparseArrayIterator implements Iterator<SparseArrayEntry<V>> {
      private int index;

      @Override
      public boolean hasNext() {
        return index < keys.length;
      }

      @Override
      public SparseArrayEntry<V> next() {
        if (!hasNext()) {
          throw new NoSuchElementException();
        }

        int key = keys[index];
        @SuppressWarnings("unchecked")
        V value = (V) values[index++];
        return SparseArrayEntry.entry(key, value);
      }
    }

    return new SparseArrayIterator();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof SparseArray)) {
      return false;
    }
    SparseArray<?> that = (SparseArray<?>) obj;

    if (keys.length != that.size()) {
      return false;
    }

    for (int i = 0; i < keys.length; i++) {
      int key = keys[i];
      @SuppressWarnings("unchecked")
      V value = (V) values[i];
      if (!Objects.equals(value, that.get(key))) {
        return false;
      }
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    for (int i = 0; i < keys.length; i++) {
      hash += SparseArrayEntry.hashCodeFor(keys[i], values[i]);
    }
    return hash;
  }

  @Override
  public String toString() {
    String delim = "";
    StringBuilder buf = new StringBuilder("[");
    for (int i = 0; i < keys.length; i++) {
      buf.append(delim)
          .append(keys[i])
          .append('=')
          .append(values[i]);
      delim = ", ";
    }
    return buf.append(']').toString();
  }

  public static <V> SparseArray<V> toImmutableSparseArray(SparseArray<V> entries) {
    int size = entries.size();
    int[] keys = new int[size];
    Object[] values = new Object[size];
    int index = 0;
    for (SparseArrayEntry<V> entry : entries) {
      keys[index] = entry.getKey();
      values[index] = entry.getValue();
    }

    return new ArrayBackedSparseArray<>(keys, values);
  }
}
