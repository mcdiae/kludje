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

import java.util.Collection;
import java.util.Iterator;

abstract class AbstractMutableCollection<E> extends MutableStore implements Collection<E> {

  @Override
  public Iterator<E> iterator() {
    Iterator<E> iter = (Iterator<E>) super.storeIterator();
    return iter;
  }

  @Override
  public void clearElements() {
    super.clearElements();
  }

  @Override
  public boolean contains(Object o) {
    return super.contains(o);
  }

  @Override
  public int size() {
    return super.size();
  }

  @Override
  public boolean isEmpty() {
    return super.isEmpty();
  }

  @Override
  public Object[] toArray() {
    return super.toArray();
  }

  @Override
  public <T> T[] toArray(T[] a) {
    return super.toArray(a);
  }

  @Override
  public boolean remove(Object o) {
    return super.removeEntry(o);
  }

  @Override
  public boolean removeAll(Collection<?> c) {
    return super.removeAll(c);
  }

  @Override
  public boolean retainAll(Collection<?> c) {
    return super.retainAll(c);
  }

  @Override
  public boolean containsAll(Collection<?> c) {
    return super.containsAll(c);
  }

  @Override
  public boolean addAll(Collection<? extends E> c) {
    boolean changed = false;
    for (E e : c) {
      changed |= add(e);
    }
    return changed;
  }
}
