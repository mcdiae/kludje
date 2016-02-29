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

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.stream.IntStream;

/**
 * Immutable {@link SparseArray} with one entry.
 */
final class SingletonSparseArray<V> implements SparseArray<V> {

  private final int key;
  private final V value;
  private SparseArrayEntry<V> entry;

  private SingletonSparseArray(int key, V value) {
    this.key = key;
    this.value = value;
  }

  @Override
  public V get(int key) {
    return (key == this.key) ? value : null;
  }

  @Override
  public int size() {
    return 1;
  }

  @Override
  public boolean contains(int key) {
    return key == this.key;
  }

  @Override
  public IntStream keyStream() {
    return IntStream.of(key);
  }

  @Override
  public Iterator<SparseArrayEntry<V>> iterator() {
    SparseArrayEntry<V> serve = entry;
    if (serve == null) {
      serve = SparseArrayEntry.entry(key, value);
      entry = serve;
    }

    class SparseArrayIterator implements Iterator<SparseArrayEntry<V>> {

      private boolean served;

      @Override
      public boolean hasNext() {
        return !served;
      }

      @Override
      public SparseArrayEntry<V> next() {
        if (served) {
          throw new NoSuchElementException();
        }
        served = true;
        return entry;
      }
    }

    return new SparseArrayIterator();
  }

  public static <V> SparseArray<V> singletonSparseArray(int key, V value) {
    return new SingletonSparseArray<>(key, value);
  }
}
