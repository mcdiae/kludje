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

import uk.kludje.collect.fixed.FixedCollection;
import uk.kludje.collect.fixed.FixedMap;
import uk.kludje.collect.fixed.FixedSet;

import java.util.AbstractMap;

/**
 * Created by user on 28/12/15.
 */
final class EmptyFixedMap<K, V> extends AbstractMap<K, V> implements FixedMap<K, V> {

  private static final FixedMap<?, ?> INSTANCE = new EmptyFixedMap<>();

  private EmptyFixedMap() {}

  @Override
  public FixedSet<Entry<K, V>> entrySet() {
    return EmptyFixedSet.emptyFixedSet();
  }

  @Override
  public FixedCollection<V> values() {
    return EmptyFixedList.emptyFixedList();
  }

  @Override
  public FixedSet<K> keySet() {
    return EmptyFixedSet.emptyFixedSet();
  }

  public static <K, V> FixedMap<K, V> emptyFixedMap() {
    @SuppressWarnings("unchecked")
    FixedMap<K, V> map = (FixedMap<K, V>) INSTANCE;
    return map;
  }
}
