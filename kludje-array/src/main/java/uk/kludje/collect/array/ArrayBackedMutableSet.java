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

import java.util.Objects;
import java.util.Set;

final class ArrayBackedMutableSet<E> extends AbstractMutableCollection<E> implements Set<E> {

  @Override
  public boolean add(E element) {
    assert element != this: "element != this";

    boolean contains = contains(element);
    if (contains) {
      return false;
    } else {
      ensureFreeCapacity(1);
      if (start == 0) {
        elements[end++] = element;
      } else {
        elements[--start] = element;
      }
      return true;
    }
  }

  protected void ensureFreeCapacity(int n) {
    assert n >= 0: "n >= 0";

    version++;

    if (n < start) {
      return;
    }

    ensureFreeTailCapacity(n);
  }

  private void ensureFreeTailCapacity(int n) {
    assert n >= 0: "n >= 0";

    version++;

    int endCapacity = elements.length - end;
    if (n <= endCapacity) {
      // have enough room
      return;
    }

    int size = size();
    int capacity = endCapacity + start;
    if (n <= capacity) {
      defrag();
      // make room at the end
      return;
    }
    // increase space
    int increase = growSizeBy(n - size);
    int newSize = increase + size;
    Assert.that(newSize >= (size + n), "newSize >= (size + n)");
    Object[] newArray = new Object[newSize];
    System.arraycopy(elements, start, newArray, 0, size);
    start = 0;
    end = size;
    elements = newArray;
  }

  private void defrag() {
    version++;
    int size = size();
    System.arraycopy(elements, start, elements, 0, size);
    start = 0;
    end = size;
  }

  @Override
  public void clear() {
    clearElements();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }

    return obj instanceof Set
        && areEqual((Set<?>) obj);
  }

  private boolean areEqual(Set<?> that) {
    for (Object element : this) {
      if (!that.contains(element)) {
        return false;
      }
    }
    for (Object element : that) {
      if (!contains(element)) {
        return false;
      }
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    for (Object element : this) {
      hash += Objects.hashCode(element);
    }
    return hash;
  }
}
