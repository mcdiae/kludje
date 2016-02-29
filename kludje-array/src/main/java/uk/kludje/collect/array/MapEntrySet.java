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
 * TODO: deprecate some default methods
 */
@Deprecated
interface MapEntrySet<K, V> extends Set<Map.Entry<K, V>> {

  @Override
  @Deprecated
  default boolean add(Map.Entry<K, V> kvEntry)  {
    throw new UnsupportedOperationException();
  }

  @Override
  @Deprecated
  default boolean addAll(Collection<? extends Map.Entry<K, V>> c)  {
    throw new UnsupportedOperationException();
  }
}
