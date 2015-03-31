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
import java.util.function.BooleanSupplier;
import java.util.function.IntFunction;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

/**
 * Convenience methods for producing collection types.
 */
public class AdapterCollections {
  private AdapterCollections() {
  }

  public static <E> Iterator<E> iterator(BooleanSupplier hasNext, Supplier<E> next) {
    return new UnmodifiableIterator<>(hasNext, next);
  }

  public static <E> Collection<E> collection(Supplier<Iterator<E>> iterator, IntSupplier size) {
    return new ImmutableCollection(iterator, size);
  }

  public static <E> List<E> list(IntFunction<E> get, IntSupplier size) {
    return new ImmutableList<>(get, size);
  }

  public static <E> Set<E> set(Supplier<Iterator<E>> iterator, IntSupplier size) {
    return new ImmutableSet(iterator, size);
  }

  private static class UnmodifiableIterator<E> implements Iterator<E> {
    private final BooleanSupplier hasNext;
    private final Supplier<E> next;

    private UnmodifiableIterator(BooleanSupplier hasNext, Supplier<E> next) {
      Objects.requireNonNull(hasNext, "hasNext");
      Objects.requireNonNull(next, "next");
      this.hasNext = hasNext;
      this.next = next;
    }

    @Override
    public boolean hasNext() {
      return hasNext.getAsBoolean();
    }

    @Override
    public E next() {
      return next.get();
    }
  }

  private static class ImmutableCollection<E> extends AbstractCollection<E> {
    private final Supplier<Iterator<E>> iterator;
    private final IntSupplier size;

    private ImmutableCollection(Supplier<Iterator<E>> iterator, IntSupplier size) {
      Objects.requireNonNull(iterator, "iterator");
      Objects.requireNonNull(size, "size");
      this.iterator = iterator;
      this.size = size;
    }

    @Override
    public Iterator<E> iterator() {
      return iterator.get();
    }

    @Override
    public int size() {
      return size.getAsInt();
    }
  }

  private static class ImmutableList<E> extends AbstractList<E> {
    private final IntFunction<E> get;
    private final IntSupplier size;

    private ImmutableList(IntFunction<E> get, IntSupplier size) {
      Objects.requireNonNull(get, "get");
      Objects.requireNonNull(size, "size");
      this.get = get;
      this.size = size;
    }

    @Override
    public E get(int index) {
      return get.apply(index);
    }

    @Override
    public int size() {
      return size.getAsInt();
    }
  }

  private static class ImmutableSet<E> extends AbstractSet<E> {
    private final Supplier<Iterator<E>> iterator;
    private final IntSupplier size;

    private ImmutableSet(Supplier<Iterator<E>> iterator, IntSupplier size) {
      Objects.requireNonNull(iterator, "iterator");
      Objects.requireNonNull(size, "size");
      this.iterator = iterator;
      this.size = size;
    }

    @Override
    public Iterator<E> iterator() {
      return iterator.get();
    }

    @Override
    public int size() {
      return size.getAsInt();
    }
  }
}
