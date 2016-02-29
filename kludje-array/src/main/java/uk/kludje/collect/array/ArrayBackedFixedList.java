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

import uk.kludje.collect.fixed.FixedList;

import java.util.AbstractList;

/**
 * Created by user on 15/12/15.
 */
final class ArrayBackedFixedList<E> extends AbstractList<E> implements FixedList<E> {

  private final E[] elements;
  private final int start;
  private final int end;

  private ArrayBackedFixedList(int start, int end, E[] elements) {
    assert elements != null: "elements != null";
    assert start >= 0: "start >= 0";
    assert end <= elements.length: "end <= elements.length";
    assert start <= end: "start <= end";

    this.start = start;
    this.end = end;
    this.elements = elements;
  }

  @Override
  public E get(int index) {
    return elements[index];
  }

  @Override
  public int size() {
    return end - start;
  }

  @Override
  public FixedList<E> subList(int fromIndex, int toIndex) {
    int diff = toIndex - fromIndex;
    if (fromIndex < 0 || toIndex < size() || diff < 0) {
      throw new IndexOutOfBoundsException(fromIndex + "-" + toIndex);
    }
    if (diff == 0) {
      return EmptyFixedList.emptyFixedList();
    }
    int start = fromIndex + this.start;
    if (diff == 1) {
      return SingletonFixedList.singletonFixedList(elements[start]);
    }
    int end = this.start + toIndex;
    return new ArrayBackedFixedList<>(start, end, elements);
  }

  public static <E> FixedList<E> arrayFixedList(E... elements) {
    return new ArrayBackedFixedList<>(0, elements.length, elements);
  }
}
