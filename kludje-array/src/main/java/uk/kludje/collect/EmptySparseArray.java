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

import uk.kludje.collect.array.SparseArrayEntry;

import java.util.Collections;
import java.util.Iterator;
import java.util.stream.IntStream;

/**
 * Immutable {@link SparseArray} with one entry.
 */
final class EmptySparseArray<V> implements SparseArray<V> {

  private static final SparseArray<?> INSTANCE = new EmptySparseArray<>();

  @Override
  public V get(int key) {
    return null;
  }

  @Override
  public int size() {
    return 0;
  }

  @Override
  public boolean contains(int key) {
    return false;
  }

  @Override
  public IntStream keyStream() {
    return IntStream.empty();
  }

  @Override
  public Iterator<SparseArrayEntry<V>> iterator() {
    return Collections.emptyIterator();
  }

  public static <V> SparseArray<V> emptySparseArray() {
    @SuppressWarnings("unchecked")
    SparseArray<V> empty = (SparseArray<V>) INSTANCE;
    return empty;
  }
}
