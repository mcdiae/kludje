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

package uk.kludje.experimental.collect.fixed;

import java.util.Collection;
import java.util.function.Predicate;

/**
 * Type for contract documentation.
 */
public interface FixedCollection<E> extends Collection<E> {

  @Override
  @Deprecated
  default boolean add(E e) throws UnsupportedOperationException {
    throw new UnsupportedOperationException();
  }

  @Override
  @Deprecated
  default boolean remove(Object o) throws UnsupportedOperationException {
    throw new UnsupportedOperationException();
  }

  @Override
  @Deprecated
  default boolean addAll(Collection<? extends E> c) throws UnsupportedOperationException {
    throw new UnsupportedOperationException();
  }

  @Override
  @Deprecated
  default boolean removeAll(Collection<?> c) throws UnsupportedOperationException {
    throw new UnsupportedOperationException();
  }

  @Override
  @Deprecated
  default boolean removeIf(Predicate<? super E> filter) throws UnsupportedOperationException {
    throw new UnsupportedOperationException();
  }

  @Override
  @Deprecated
  default boolean retainAll(Collection<?> c) throws UnsupportedOperationException {
    throw new UnsupportedOperationException();
  }

  @Override
  @Deprecated
  default void clear() throws UnsupportedOperationException {
    throw new UnsupportedOperationException();
  }
}
