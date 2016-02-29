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

import java.util.Map;
import java.util.Objects;

/**
 * Created by user on 28/12/15.
 */
abstract class AbstractMapEntry<K, V> implements Map.Entry<K, V> {
  protected final K key;
  protected V value;

  protected AbstractMapEntry(K key, V value) {
    this.key = key;
    this.value = value;
  }

  @Override
  public K getKey() {
    return key;
  }

  @Override
  public V getValue() {
    return value;
  }

  @Override
  public boolean equals(Object obj) {
    return (obj instanceof Map.Entry)
        && isEqual((Map.Entry<?, ?>) obj);
  }

  private boolean isEqual(Map.Entry<?, ?> that) {
    if (that == this) {
      return true;
    }

    return Objects.equals(key, that.getKey())
        && Objects.equals(value, that.getValue());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(key) ^ Objects.hashCode(value);
  }

  @Override
  public String toString() {
    return key + "=" + getValue();
  }
}
