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

import uk.kludje.experimental.collect.fixed.FixedSet;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Objects;

/**
 * Created by user on 15/12/15.
 */
class SingletonFixedSet<E> extends AbstractSet<E> implements FixedSet<E> {

  private final E element;

  private SingletonFixedSet(E element) {
    this.element = element;
  }

  @Override
  public boolean contains(Object o) {
    return Objects.equals(element, o);
  }

  @Override
  public Iterator<E> iterator() {
    return new SingletonIterator<E>(element);
  }

  @Override
  public int size() {
    return 1;
  }

  public static <E> FixedSet<E> singleton(E element) {
    return new SingletonFixedSet<>(element);
  }
}
