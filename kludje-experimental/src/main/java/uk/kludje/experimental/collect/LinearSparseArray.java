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

import java.util.Arrays;
import java.util.List;

class LinearSparseArray<T> extends AbstractSparseArray<T> implements SparseArray<T> {

  private int[] keys = EmptyArrays.EMPTY_INT;
  private Object[] values = EmptyArrays.EMPTY_OBJECT;
  private int size;
  private int increment;

  public LinearSparseArray(int increment) {
    Assert.that(increment > 0, "increment > 0");
    this.increment = increment;
  }

  @Override
  public T get(int key) {
    int index = ArraySearch.linearSearch(keys, size, key);
    @SuppressWarnings("unchecked")
    T value = (index < 0) ? null : (T) values[index];
    return value;
  }

  @Override
  public T put(int key, T value) {
    if (value == null) {
      return remove(key);
    }

    int replaceIndex = ArraySearch.linearSearch(keys, size, key);

    if (replaceIndex >= 0) {
      @SuppressWarnings("unchecked")
      T old = (T) values[replaceIndex];
      values[replaceIndex] = value;
      return old;
    }

    if (size == keys.length) {
      int newlen = size + increment;
      keys = Arrays.copyOf(keys, newlen);
      values = Arrays.copyOf(values, newlen);
    }

    keys[size] = key;
    values[size] = value;
    size++;

    return null;
  }

  @Override
  public T remove(int key) {
    int removeIndex = ArraySearch.linearSearch(keys, size, key);
    if (removeIndex < 0) {
      return null;
    }
    @SuppressWarnings("unchecked")
    T value = (T) values[removeIndex];
    int after = keys.length - removeIndex - 1;
    int offset = removeIndex + 1;
    System.arraycopy(keys, offset, keys, removeIndex, after);
    System.arraycopy(values, offset, values, removeIndex, after);
    size--;
    return value;
  }

  @Override
  public int size() {
    return size;
  }

  @Override
  public List<T> values() {
    @SuppressWarnings("unchecked")
    List<T> list = (List<T>) Arrays.asList(values);
    return list;
  }

  @Override
  public IntSet keys() {
    return LambdaIntSets.toArrayIntSet(keys, 0, size);
  }

  @Override
  public int keyAt(int index) {
    return keys[index];
  }
}
