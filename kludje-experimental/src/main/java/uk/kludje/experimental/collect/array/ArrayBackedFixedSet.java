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

import uk.kludje.experimental.array.LinearSearch;
import uk.kludje.experimental.collect.fixed.FixedSet;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * Created by user on 27/12/15.
 */
final class ArrayBackedFixedSet<E> extends AbstractSet<E> implements FixedSet<E> {

  private final Object[] elements;

  private ArrayBackedFixedSet(Object... elements) {
    this.elements = elements;
  }

  @Override
  public boolean contains(Object o) {
    return LinearSearch.find(elements, 0, elements.length, o)
        != LinearSearch.NOT_FOUND;
  }

  @Override
  public Iterator<E> iterator() {
    return new ArrayIterator(elements);
  }

  @Override
  public int size() {
    return elements.length;
  }

  public static <E> FixedSet<E> toFixedSet(Set<? extends E> set) {
    return new ArrayBackedFixedSet<>(set.toArray());
  }

  private static class ArrayIterator<E> implements Iterator<E> {
    private final Object[] elements;
    private int index;

    private ArrayIterator(Object... elements) {
      this.elements = elements;
    }

    @Override
    public boolean hasNext() {
      return index < elements.length;
    }

    @Override
    public E next() {
      if (!hasNext()) {
        throw new NoSuchElementException();
      }
      @SuppressWarnings("unchecked")
      E element = (E) elements[index++];
      return element;
    }
  }
}
