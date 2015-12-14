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

package uk.kludje.array;

/**
 * Created by user on 12/12/15.
 */
public final class ArrayCollections {

  private ArrayCollections() {}

  public static <V> MutableSparseArray<V> mutableSparseArray() {
    return MutableSparseArrayImpl.sparseArray();
  }

  public static <V> SparseArray<V> toImmutableSparseArray(SparseArray<V> entries) {
    int size = entries.size();
    if (size == 0) {
      return EmptySparseArray.emptySparseArray();
    }
    if (size == 1) {
      SparseArrayEntry<V> entry = entries.iterator().next();
      return SingletonSparseArray.singletonSparseArray(entry.getKey(), entry.getValue());
    }
    return ImmutableSparseArray.toImmutableSparseArray(entries);
  }

  @SafeVarargs
  public static <V> SparseArray<V> toImmutableSparseArray(SparseArrayEntry<? extends V>... entries) {
    int size = entries.length;
    if (size == 0) {
      return EmptySparseArray.emptySparseArray();
    }
    if (size == 1) {
      SparseArrayEntry<? extends V> entry = entries[0];
      return SingletonSparseArray.singletonSparseArray(entry.getKey(), entry.getValue());
    }
    return ImmutableSparseArray.toImmutableSparseArray(entries);
  }
}
