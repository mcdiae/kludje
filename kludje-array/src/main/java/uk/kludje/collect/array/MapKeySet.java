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
import java.util.Map;
import java.util.Set;

/**
 * This interface can be used to implement the {@link Map#keySet()} method.
 *
 * TODO: deprecate some Java 8 default methods
 *
 * @param <E> the key type
 */
@Deprecated
interface MapKeySet<E> extends Set<E> {

  /**
   * @param e n/a
   * @return n/a
   * @throws UnsupportedOperationException always
   */
  @Deprecated
  @Override
  default boolean add(E e) {
    throw new UnsupportedOperationException();
  }

  /**
   * @param c n/a
   * @return n/a
   * @throws UnsupportedOperationException always
   */
  @Deprecated
  @Override
  default boolean addAll(Collection<? extends E> c) {
    throw new UnsupportedOperationException();
  }
}
