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

import java.util.AbstractSet;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by user on 03/01/16.
 */
final class EmptyMapEntrySet<K, V> extends AbstractSet<Map.Entry<K, V>> implements MapEntrySet<K, V> {

  private static final MapEntrySet<?, ?> INSTANCE = new EmptyMapEntrySet<>();

  @Override
  public Iterator<Map.Entry<K, V>> iterator() {
    return Collections.emptyIterator();
  }

  @Override
  public int size() {
    return 0;
  }

  public static <K, V> EmptyMapEntrySet<K, V> emptyMapEntrySet() {
    @SuppressWarnings("unchecked")
    EmptyMapEntrySet<K, V> set = (EmptyMapEntrySet<K, V>) INSTANCE;
    return set;
  }
}
