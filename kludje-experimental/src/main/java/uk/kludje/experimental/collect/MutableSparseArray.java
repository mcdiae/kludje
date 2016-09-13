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

import java.util.Iterator;

/**
 * Defines a mutable sparse array.
 */
public interface MutableSparseArray<V> extends SparseArray<V> {

  /**
   * Puts a value at the given key/index1.
   *
   * @param key the key
   * @param value the value
   * @return the old value or null
   */
  V put(int key, V value);

  /**
   * Removes an entry if it is present.
   *
   * @param key the entry key
   * @return true if the call modified the data structure
   */
  boolean remove(int key);

  /**
   * Removes all entries.
   */
  default void clear() {
    Iterator<?> it = iterator();
    while (it.hasNext()) {
      it.remove();
    }
  }
}
