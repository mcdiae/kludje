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

package uk.kludje.experimental.collect;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

public final class LambdaMaps {

  private LambdaMaps() {
  }

  public static <K, V> Map<K, V> map(Set<K> keySet, Function<Object, V> get) {
    Objects.requireNonNull(keySet, "keySet");
    Objects.requireNonNull(get, "get");

    Set<K> keys = Collections.unmodifiableSet(keySet);

    class LambdaMap extends AbstractMap<K, V> {

      @Override
      public int size() {
        return keySet.size();
      }

      @Override
      public boolean isEmpty() {
        return keySet.isEmpty();
      }

      @Override
      public boolean containsKey(Object key) {
        return keySet.contains(key);
      }

      @Override
      public boolean containsValue(Object value) {
        return keySet.stream()
            .map(get)
            .anyMatch((V v) -> Objects.equals(v, value));
      }

      @Override
      public V get(Object key) {
        return get.apply(key);
      }

      @Override
      public Set<K> keySet() {
        return keys;
      }

      @Override
      public Collection<V> values() {
        return LambdaCollections.<K, V>mapped(keySet, get);
      }

      @Override
      public Set<Entry<K, V>> entrySet() {
        return LambdaSets.set(this::entries, keySet::size);
      }

      private Iterator<Entry<K, V>> entries() {
        Iterator<K> keyIt = keySet.iterator();
        Supplier<Entry<K, V>> entries = () -> {
          K key = keyIt.next();
          V value = get.apply(key);
          return new SimpleImmutableEntry<>(key, value);
        };
        return LambdaIterators.iterator(keyIt::hasNext, entries);
      }
    }

    return new LambdaMap();
  }
}
