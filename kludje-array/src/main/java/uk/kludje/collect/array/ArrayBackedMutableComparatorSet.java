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

import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;
import java.util.Set;

final class ArrayBackedMutableComparatorSet<E> extends AbstractArrayCollection<E> implements Set<E> {

  private final Comparator<E> comparator;

  public ArrayBackedMutableComparatorSet(Comparator<E> comparator) {
    Assert.that(comparator != null, "comparator != null");

    this.comparator = comparator;
  }

  @Override
  public boolean add(E element) {
    assert element != this: "element != this";

    int low = start;
    int high = end - 1;

    int position = 0;
    while (low <= high) {
      int mid = (low + high) >>> 1;
      E midVal = (E) elements[mid];
      int cmp = comparator.compare(midVal, element);
      if (cmp < 0) {
        low = mid + 1;
        position = low;
      } else if (cmp > 0) {
        high = mid - 1;
        position = high;
      } else {
        return false;
      }
    }

    ensureFreeCapacity(1);
    if (start > 0) {
      System.arraycopy(elements, start, elements, start - 1, position - start);
      start--;
    } else {
      System.arraycopy(elements, position, elements, position + 1, end - position);
      end++;
    }
    elements[position] = element;
    return true;
  }

  protected int indexOf(Object o) {
    @SuppressWarnings("unchecked")
    Comparator<Object> c = (Comparator<Object>) comparator;
    return Arrays.binarySearch(this.elements, start, end, o, c);
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
