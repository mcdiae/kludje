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

import java.util.Iterator;
import java.util.Objects;
import java.util.stream.IntStream;

/**
 * A simple set-of-integers type.
 * There is no guarantee of order.
 */
public interface IntSet extends Iterable<Integer> {

  public static final IntSet EMPTY = IntSets.arrayIntList();

  int size();

  int intAt(int index);

  @Override
  boolean equals(Object o);

  @Override
  int hashCode();

  default boolean contains(int n) {
    int len = size();
    for (int i = 0; i < len; i++) {
      if (n == intAt(i)) {
        return true;
      }
    }
    return false;
  }

  @Override
  default Iterator<Integer> iterator() {
    int size = size();
    return (size == 0)
        ? LambdaIterators.emptyIterator()
        : LambdaIterators.indexIterator(i -> (i < size()), this::intAt);
  }

  default IntStream stream() {
    return IntStream.range(0, size())
        .map(this::intAt);
  }
}
