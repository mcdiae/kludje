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

import uk.kludje.array.EmptyArrays;

import java.util.*;

abstract class MutableStore {

  protected Object[] elements = EmptyArrays.EMPTY_OBJECT_ARRAY;
  protected int start;
  protected int end;
  protected int version;

  protected void checkVersion(int version) {
    if (version != this.version) {
      String threadName = Thread.currentThread().getName();
      throw new ConcurrentModificationException(threadName);
    }
  }

  protected int growSizeBy(int requiredCapacity) {
    assert requiredCapacity >= 0 : "requiredCapacity >= 0";

    int size = size();
    int standardIncrease = (size == 0) ? 8 : size;

    if (requiredCapacity > standardIncrease) {
      return requiredCapacity;
    } else {
      return standardIncrease;
    }
  }

  protected Object at(int index) {
    int getAt = index + start;
    assertIndexInBounds(getAt);
    return elements[getAt];
  }

  protected int getVersion() {
    return version;
  }

  protected int size() {
    return end - start;
  }

  protected int indexOf(Object o) {
    for (int i = start; i < end; i++) {
      Object element = elements[i];
      if (Objects.equals(element, o)) {
        return i;
      }
    }
    return -1;
  }

  protected Object removeIndex(int index) {
    int removeFrom = index + start;
    assertIndexInBounds(removeFrom);
    version++;
    Object removed = elements[removeFrom];
    System.arraycopy(elements, removeFrom + 1, elements, removeFrom, end - removeFrom);
    return removed;
  }

  protected void assertIndexInBounds(int offset) {
    Assert.that(offset >= start, "offset >= start", IndexOutOfBoundsException::new);
    Assert.that(offset < end, "offset < end", IndexOutOfBoundsException::new);
  }

  protected boolean isEmpty() {
    return start == end;
  }

  protected Object[] toArray() {
    return elements.clone();
  }

  protected boolean contains(Object o) {
    return indexOf(o) != -1;
  }

  protected void clearElements() {
    Arrays.fill(elements, null);
    start = 0;
    end = 0;
  }

  protected Iterator<Object> storeIterator() {
    return new MutableStoreIterator(this);
  }

  protected boolean removeEntry(Object o) {
    return removeIndex(indexOf(o)) != null;
  }

  protected boolean containsAll(Collection<?> c) {
    for (Object o : c) {
      if (contains(o)) {
        return true;
      }
    }
    return false;
  }

  protected boolean removeAll(Collection<?> c) {
    boolean removed = false;
    for (Object o : c) {
      removed |= removeEntry(o);
    }
    return removed;
  }

  protected boolean retainAll(Collection<?> c) {
    boolean changed = false;
    for (Object o : c) {
      int idx = indexOf(o);
      if (idx >= 0) {
        changed = true;
        removeIndex(idx);
      }
    }
    return changed;
  }

  protected <T> T[] toArray(T[] a) {
    //noinspection SuspiciousSystemArraycopy
    System.arraycopy(elements, start, a, 0, end - start);
    return a;
  }

  @Override
  public String toString() {
    StringJoiner joiner = new StringJoiner(", ", "[", "]");
    for (int i = start; i < end; i++) {
      joiner.add(Objects.toString(elements[i]));
    }
    return joiner.toString();
  }
}