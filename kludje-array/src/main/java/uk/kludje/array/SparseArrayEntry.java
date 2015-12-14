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

import java.util.Objects;

public final class SparseArrayEntry<V> {

  private final int key;
  private final V value;

  private SparseArrayEntry(int key, V value) {
    this.key = key;
    this.value = value;
  }

  public int getKey() {
    return key;
  }

  public V getValue() {
    return value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    SparseArrayEntry<?> that = (SparseArrayEntry<?>) o;

    if (key != that.key) return false;
    return Objects.equals(this.value, that.value);
  }

  @Override
  public int hashCode() {
    return hashCodeFor(key, value);
  }

  public static int hashCodeFor(int key, Object value) {
    int result = key;
    result = 31 * result + Objects.hashCode(value);
    return result;
  }

  @Override
  public String toString() {
    return key + "=" + value;
  }

  public static <V> SparseArrayEntry<V> entry(int key, V value) {
    return new SparseArrayEntry<>(key, value);
  }
}
