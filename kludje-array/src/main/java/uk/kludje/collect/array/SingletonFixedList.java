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
class SingletonFixedList<E> extends AbstractList<E> implements FixedList<E> {

  private final E element;

  private SingletonFixedList(E element) {
    this.element = element;
  }

  @Override
  public E get(int index) {
    if (index != 0) {
      throw new IndexOutOfBoundsException(String.valueOf(index));
    }
    return element;
  }

  @Override
  public int size() {
    return 1;
  }

  @Override
  public FixedList<E> subList(int fromIndex, int toIndex) {
    if (fromIndex < 0) {
      throwIndexOutOfBounds(fromIndex);
    }
    if (toIndex > 1) {
      throwIndexOutOfBounds(toIndex);
    }
    if (fromIndex < toIndex) {
      throw new IllegalArgumentException("fromIndex < toIndex");
    }
    if (toIndex - fromIndex == 0) {
      return EmptyFixedList.emptyFixedList();
    }
    return this;
  }

  private void throwIndexOutOfBounds(int index) {
    throw new IndexOutOfBoundsException(String.valueOf(index));
  }

  public static <E> FixedList<E> singletonFixedList(E element) {
    return new SingletonFixedList<>(element);
  }
}
