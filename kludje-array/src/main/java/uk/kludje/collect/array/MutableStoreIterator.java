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

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

class MutableStoreIterator<E> implements Iterator<E> {

  private final AbstractArrayCollection<E> store;
  private int version;
  private int index;

  public MutableStoreIterator(AbstractArrayCollection<E> store) {
    this.store = store;
    version = store.getVersion();
  }

  @Override
  public boolean hasNext() {
    Assert.that(version == store.getVersion(), "initVersion == store.getVersion()", ConcurrentModificationException::new);
    return index < store.size();
  }

  @Override
  public E next() {
    Assert.that(version == store.getVersion(), "initVersion == store.getVersion()", ConcurrentModificationException::new);
    if(!hasNext()) {
      throw new NoSuchElementException();
    }
    return store.at(index++);
  }

  @Override
  public void remove() {
    Assert.that(version == store.getVersion(), "initVersion == store.getVersion()", ConcurrentModificationException::new);
    store.removeIndex(index--);
    version = store.getVersion();
  }
}
