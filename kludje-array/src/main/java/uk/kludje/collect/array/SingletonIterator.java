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

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by user on 27/12/15.
 */
final class SingletonIterator<E> implements Iterator<E> {

  private final E element;
  private boolean served;

  public SingletonIterator(E element) {
    this.element = element;
  }

  @Override
  public boolean hasNext() {
    return !served;
  }

  @Override
  public E next() {
    if (served) {
      throw new NoSuchElementException();
    }
    return element;
  }
}
