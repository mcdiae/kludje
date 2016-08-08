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
import java.util.Collections;
import java.util.Iterator;

@SuppressWarnings("deprecation")
final class EmptyFixedSet<E> extends AbstractSet<E> implements FixedSet<E> {

  private static final FixedSet<?> INSTANCE = new EmptyFixedSet<>();

  @Override
  public boolean contains(Object o) {
    return false;
  }

  @Override
  public Iterator<E> iterator() {
    return Collections.emptyIterator();
  }

  @Override
  public int size() {
    return 0;
  }

  public static <E> FixedSet<E> emptyFixedSet() {
    @SuppressWarnings("unchecked")
    FixedSet<E> set = (FixedSet<E>) INSTANCE;
    return set;
  }
}
