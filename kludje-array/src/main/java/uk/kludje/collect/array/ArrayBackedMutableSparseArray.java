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

package uk.kludje.collect.array;

import uk.kludje.array.EmptyArrays;
import uk.kludje.array.LinearSearch;
import uk.kludje.collect.MutableSparseArray;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.stream.IntStream;

/**
 * Created by user on 12/12/15.
 */
final class ArrayBackedMutableSparseArray<V> extends MutableStore implements MutableSparseArray<V> {

  private int[] keys = EmptyArrays.EMPTY_INT_ARRAY;

  @Override
  public V put(int key, V value) {
    assert value != this: "value != this";

    // first check if this is a replace operation
    int index = LinearSearch.find(keys, start, end, key);
    if (index != LinearSearch.NOT_FOUND) {
      @SuppressWarnings("unchecked")
      V old = (V) elements[index];
      elements[index] = value;
      return old;
    }
    // add
    version++;
    ensureFreeCapacity(1);
    if (start > 0) {
      start--;
      keys[start] = key;
      elements[start] = value;
    } else {
      keys[end] = key;
      elements[end] = value;
      end++;
    }
    return null;
  }

  protected void ensureFreeCapacity(int growBy) {
    assert growBy >= 0: "growBy >= 0";

    version++;

    if (start > growBy) {
      return;
    }

    int endCapacity = elements.length - end;
    if (growBy <= endCapacity) {
      return;
    }

    int size = size();
    int capacity = endCapacity + start;
    if (growBy <= capacity) {
      System.arraycopy(keys, start, keys, 0, size);
      System.arraycopy(elements, start, elements, 0, size);
      start = 0;
      end = size;
      return;
    }

    int increase = growSizeBy(growBy);
    int newSize = increase + size;
    assert newSize >= (size + growBy): "newSize >= (size + growBy)";
    int[] newKeys = new int[newSize];
    Object[] newValues = new Object[newSize];
    System.arraycopy(keys, start, newKeys, 0, size);
    System.arraycopy(elements, start, newValues, 0, size);
    start = 0;
    end = size;
    keys = newKeys;
    elements = newValues;
  }

  @Override
  public boolean remove(int key) {
    int index = LinearSearch.find(keys, start, end, key);
    if (index == LinearSearch.NOT_FOUND) {
      return false;
    }
    // found
    version++;
    if (index == start) {
      start++;
      elements[index] = null;
    } else if (index == (end - 1)) {
      end--;
      elements[index] = null;
    } else {
      System.arraycopy(keys, index + 1, keys, index, end - index);
      System.arraycopy(elements, index + 1, elements, index, end - index);
      end--;
      elements[index] = null;
    }

    return true;
  }

  @Override
  public V get(int key) {
    int index = LinearSearch.find(keys, start, end, key);
    @SuppressWarnings("unchecked")
    V value = (index == LinearSearch.NOT_FOUND) ? null : (V) elements[index];
    return value;
  }

  @Override
  public int size() {
    return super.size();
  }

  @Override
  public boolean contains(int key) {
    return LinearSearch.find(keys, start, end, key) != LinearSearch.NOT_FOUND;
  }

  @Override
  public IntStream keyStream() {
    return IntStream.of(keys)
        .skip(start)
        .limit(size());
  }

  @Override
  public boolean isEmpty() {
    return super.isEmpty();
  }

  @Override
  public Iterator<SparseArrayEntry<V>> iterator() {
    class SparseArrayIterator implements Iterator<SparseArrayEntry<V>> {
      private int version = getVersion();
      private int index = 0;

      @Override
      public boolean hasNext() {
        checkVersion(version);
        return index < size();
      }

      @Override
      public SparseArrayEntry<V> next() {
        if (!hasNext()) {
          throw new NoSuchElementException(String.valueOf(index));
        }
        int key = keys[index + start];
        @SuppressWarnings("unchecked")
        V value = (V) elements[index + start];
        return SparseArrayEntry.entry(key, value);
      }

      @Override
      public void remove() {
        checkVersion(version);
        int indexToRemove = start + index--;
        ArrayBackedMutableSparseArray.this.remove(indexToRemove);
        version = getVersion();
      }
    }

    return new SparseArrayIterator();
  }

  public static <V> MutableSparseArray<V> sparseArray() {
    return new ArrayBackedMutableSparseArray<>();
  }
}
