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
import java.util.Map;
import java.util.Set;

/**
 * Created by user on 13/12/15.
 */
class MutableMap<K, V> extends MutableStore implements Map<K, V> {

  private Object[] keys = EmptyArrays.EMPTY_OBJECT_ARRAY;

  @Override
  public boolean containsKey(Object key) {
    return false;
  }

  @Override
  public boolean containsValue(Object value) {
    return false;
  }

  @Override
  public V get(Object key) {
    int index = Linear.linearSearch(keys, start, end, key);
    if (index == Linear.NOT_FOUND) {
      return null;
    }
    @SuppressWarnings("unchecked")
    V value = (V) elements[index];
    return value;
  }

  @Override
  public V put(K key, V value) {
    int index = Linear.linearSearch(keys, start, end, key);
    if (index != Linear.NOT_FOUND) {
      @SuppressWarnings("unchecked")
      V old = (V) elements[index];
      elements[index] = value;
      return old;
    }
    // TODO: add
    return null;
  }

  @Override
  public V remove(Object key) {
    return null;
  }

  @Override
  public void putAll(Map<? extends K, ? extends V> m) {

  }

  @Override
  public Set<K> keySet() {
    return null;
  }

  @Override
  public Collection<V> values() {
    return null;
  }

  @Override
  public Set<Entry<K, V>> entrySet() {
    return null;
  }

  @Override
  public void clear() {

    super.clear();
  }

  @Override
  public int size() {
    return super.size();
  }

  @Override
  public boolean isEmpty() {
    return super.isEmpty();
  }
}
