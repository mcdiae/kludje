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

import uk.kludje.collect.fixed.FixedList;
import uk.kludje.collect.fixed.FixedSet;
import uk.kludje.collect.MutableSparseArray;
import uk.kludje.collect.SparseArray;

import java.util.*;

/**
 * Collections backed by arrays.
 */
public final class ArrayCollections {

  private ArrayCollections() {}

  /**
   * @param <V> the value type
   * @return a new mutable sparse array
   */
  public static <V> MutableSparseArray<V> mutableSparseArray() {
    return ArrayBackedMutableSparseArray.sparseArray();
  }

  /**
   *
   *
   * @param <E> the element type
   * @return a new set backed by an array
   */
  public static <E> Set<E> mutableSet() {
    return new ArrayBackedMutableSet<>();
  }

  public static <E extends Comparable<E>> Set<E> mutableSortedSet() {
    return new ArrayBackedMutableComparatorSet<>(Comparable::compareTo);
  }

  public static <E> Set<E> mutableSortedSet(Comparator<E> comparator) {
    return new ArrayBackedMutableComparatorSet<>(comparator);
  }

  public static <K, V> Map<K, V> mutableMap() {
    return new ArrayBackedMutableMap<>();
  }

  public static <E> FixedList<E> toFixedList(Iterable<? extends E> source) {
    if (source instanceof FixedList<?>) {
      @SuppressWarnings("unchecked")
      FixedList<E> list = (FixedList<E>) source;
      return list;
    }

    if (source instanceof Collection<?>) {
      @SuppressWarnings("unchecked")
      Collection<? extends E> collection = (Collection<? extends E>) source;
      return toFixedList(collection);
    }

    List<E> list = new ArrayList<>();
    for (E element : source) {
      list.add(element);
    }
    return toFixedList(list);
  }

  private static <E> FixedList<E> toFixedList(Collection<? extends E> collection) {
    int size = collection.size();
    if (size == 0) {
      return EmptyFixedList.emptyFixedList();
    } else if (size == 1) {
      E element = collection.iterator().next();
      return SingletonFixedList.singletonFixedList(element);
    }
    FixedList<?> arrayFixedList = ArrayBackedFixedList.arrayFixedList(collection.toArray());
    @SuppressWarnings("unchecked")
    FixedList<E> fixedList = (FixedList<E>) arrayFixedList;
    return fixedList;
  }

  public static <E> FixedSet<E> toFixedSet(Iterable<? extends E> source) {
    if (source instanceof FixedSet<?>) {
      @SuppressWarnings("unchecked")
      FixedSet<E> set = (FixedSet<E>) source;
      return set;
    }

    if (source instanceof Collection<?>) {
      Collection<? extends E> coll = (Collection<? extends E>) source;
      return toFixedSet(coll);
    }

    Set<E> set = new ArrayBackedMutableSet<>();
    for (E element : source) {
      set.add(element);
    }
    return toFixedSet(set);
  }

  private static <E> FixedSet<E> toFixedSet(Collection<? extends E> collection) {
    if (collection.isEmpty()) {
      return EmptyFixedSet.emptyFixedSet();
    }

    if (collection.size() == 1) {
      E element = collection.iterator().next();
      return SingletonFixedSet.singleton(element);
    }

    if (collection instanceof Set<?>) {
      @SuppressWarnings("unchecked")
      Set<? extends E> set = (Set<? extends E>) collection;
      return ArrayBackedFixedSet.toFixedSet(set);
    }

    Set<E> set = new ArrayBackedMutableSet<>();
    set.addAll(collection);

    return ArrayBackedFixedSet.toFixedSet(set);
  }
}
