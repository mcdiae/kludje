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

import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;

class ArrayBackedSet<E> extends AbstractSet<E> {

  @SuppressWarnings("unchecked")
  private E[] elements = (E[]) Linear.EMPTY_OBJECT_ARRAY;
  private int size;
  private int increment;

  public ArrayBackedSet(int increment) {
    Assert.that(increment > 0, "increment > 0");
    this.increment = increment;
  }

  @Override
  public boolean contains(Object o) {
    return Linear.searchUntil(size, Objects::equals, elements, o) >= 0;
  }

  @Override
  public boolean add(E e) {
    int index = Linear.searchUntil(size, Objects::equals, elements, e);
    if (index >= 0) {
      return false;
    }
    if (size == elements.length) {
      elements = Arrays.copyOf(elements, size + increment);
    }
    elements[size++] = e;
    return true;
  }

  @Override
  public boolean remove(Object o) {
    int index = Linear.searchUntil(size, Objects::equals, elements, o);
    if (index >= 0) {
      removeIndex(index);
      return true;
    }
    return false;
  }

  private void removeIndex(int index) {
    System.arraycopy(elements, index + 1, elements, index, size - index - 1);
    size--;
  }

  @Override
  public Iterator<E> iterator() {
    return LambdaIterators.mutableArrayIterator(this::removeIndex, elements);
  }

  @Override
  public int size() {
    return size;
  }
}
