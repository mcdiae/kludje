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

package uk.kludje.array;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;

final class MutableSet<E> extends AbstractMutableCollection<E> implements Set<E> {

  @Override
  public boolean add(E element) {
    boolean contains = contains(element);
    if (contains) {
      return false;
    } else {
      return super.add(element);
    }
  }

  @Override
  public boolean addAll(Collection<? extends E> c) {
    boolean changed = false;
    for (E element : c) {
      changed |= add(element);
    }
    return changed;
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
