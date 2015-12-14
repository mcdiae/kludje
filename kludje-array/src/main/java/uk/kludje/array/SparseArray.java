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

import java.util.AbstractMap;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * Created by user on 11/12/15.
 */
public interface SparseArray<V> extends Iterable<SparseArrayEntry<V>> {

  V get(int key);

  int size();

  boolean contains(int key);

  IntStream keyStream();

  default boolean isEmpty() {
    return size() == 0;
  }

  default boolean isNotEmpty() {
    return size() != 0;
  }

  boolean equals(Object o);

  int hashCode();

  String toString();
}
