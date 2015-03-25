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
import java.util.function.IntFunction;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

/**
 * Convenience methods for producing collection types.
 */
public class CollectionAdapter {
  private CollectionAdapter() {
  }

  public static <E> List<E> asList(IntFunction<E> get, IntSupplier size) {
    class ListAdapter extends AbstractList<E> {

      @Override
      public E get(int index) {
        return get.apply(index);
      }

      @Override
      public int size() {
        return size.getAsInt();
      }
    }

    return new ListAdapter();
  }

  public static <E> Set<E> asSet(Supplier<? extends Iterator<E>> iterator, IntSupplier size) {
    class SetAdapter extends AbstractSet<E> {

      @Override
      public Iterator<E> iterator() {
        return iterator.get();
      }

      @Override
      public int size() {
        return size.getAsInt();
      }
    }

    return new SetAdapter();
  }

  public static <K, V> Map<K, V> asMap(Set<Map.Entry<K, V>> set) {
    class MapAdapter extends AbstractMap<K, V> {

      @Override
      public Set<Entry<K, V>> entrySet() {
        return set;
      }
    }

    return new MapAdapter();
  }
}