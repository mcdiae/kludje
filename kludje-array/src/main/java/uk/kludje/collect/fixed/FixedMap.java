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

package uk.kludje.collect.fixed;

import java.util.Map;
import java.util.function.BiFunction;

/**
 * Type for contract documentation.
 */
public interface FixedMap<K, V> extends Map<K, V> {

  @Override
  @Deprecated
  default V put(K key, V value) throws UnsupportedOperationException {
    throw new UnsupportedOperationException();
  }

  @Override
  @Deprecated
  default boolean remove(Object key, Object value) throws UnsupportedOperationException {
    throw new UnsupportedOperationException();
  }

  @Override
  @Deprecated
  default boolean replace(K key, V oldValue, V newValue) throws UnsupportedOperationException {
    throw new UnsupportedOperationException();
  }

  @Override
  @Deprecated
  default void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) throws UnsupportedOperationException {
    throw new UnsupportedOperationException();
  }

  @Override
  @Deprecated
  default V remove(Object key) throws UnsupportedOperationException {
    throw new UnsupportedOperationException();
  }

  @Override
  @Deprecated
  default V replace(K key, V value) throws UnsupportedOperationException {
    throw new UnsupportedOperationException();
  }

  @Override
  @Deprecated
  default V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) throws UnsupportedOperationException {
    throw new UnsupportedOperationException();
  }

  @Override
  @Deprecated
  default V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) throws UnsupportedOperationException {
    throw new UnsupportedOperationException();
  }

  @Override
  @Deprecated
  default V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) throws UnsupportedOperationException {
    throw new UnsupportedOperationException();
  }

  @Override
  FixedSet<Entry<K, V>> entrySet();

  @Override
  FixedSet<K> keySet();

  @Override
  FixedCollection<V> values();
}
