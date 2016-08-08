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

package uk.kludje.experimental.collect.array;

import uk.kludje.experimental.collect.fixed.FixedList;

import java.util.AbstractList;

@SuppressWarnings("deprecation")
final class EmptyFixedList<E> extends AbstractList<E> implements FixedList<E> {

  private static final FixedList<?> INSTANCE = new EmptyFixedList<>();

  @Override
  public E get(int index) {
    throw new IndexOutOfBoundsException(String.valueOf(index));
  }

  @Override
  public int size() {
    return 0;
  }

  @Override
  public FixedList<E> subList(int fromIndex, int toIndex) {
    if (fromIndex != 0 || toIndex != 0) {
      throw new IndexOutOfBoundsException();
    }
    return this;
  }

  public static <E> FixedList<E> emptyFixedList() {
    @SuppressWarnings("unchecked")
    FixedList<E> list = (FixedList<E>) INSTANCE;
    return list;
  }
}
