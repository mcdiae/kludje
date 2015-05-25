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

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.IntStream;

/**
 * A simple set-of-ints type.
 */
public interface IntSet {

  IntStream stream();

  @Override
  boolean equals(Object o);

  @Override
  int hashCode();

  @Override
  String toString();

  default boolean contains(int n) {
    return stream().anyMatch(i -> i == n);
  }

  default int size() {
    long count = stream().count();
    assert count < Integer.MAX_VALUE;
    return (int) count;
  }

  default Set<Integer> asUnboxedSet() {
    int size = size();
    if(size == 0) {
      return Collections.emptySet();
    } else {
      Iterable<Integer> iterable = () -> stream().mapToObj(Integer::valueOf).iterator();
      return LambdaSets.set(iterable, () -> size);
    }
  }
}
