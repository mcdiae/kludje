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

import uk.kludje.Unsupported;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.function.IntSupplier;
import java.util.function.Predicate;

/**
 * Created by user on 10/04/15.
 */
public final class LambdaSets {
  private LambdaSets() {
  }

  public static <E> Set<E> mutableSet(Iterable<E> iterator,
                                      IntSupplier size,
                                      Predicate<E> add) {
    Objects.requireNonNull(iterator, "iterator");
    Objects.requireNonNull(size, "size");
    Objects.requireNonNull(add, "add");

    class LambdaSet extends AbstractSet<E> {

      @Override
      public Iterator<E> iterator() {
        return iterator.iterator();
      }

      @Override
      public int size() {
        return size.getAsInt();
      }

      @Override
      public boolean add(E e) {
        return add.test(e);
      }
    }

    return new LambdaSet();
  }

  public static <E> Set<E> set(Iterable<E> iterator,
                               IntSupplier size) {
    return mutableSet(iterator, size, Unsupported::exception);
  }
}
