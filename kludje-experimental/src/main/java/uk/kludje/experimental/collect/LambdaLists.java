/*
 * Copyright 2014 McDowell
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

import java.util.AbstractList;
import java.util.List;
import java.util.Objects;
import java.util.function.IntFunction;
import java.util.function.IntSupplier;

public final class LambdaLists {

  private LambdaLists() {
  }

  public static <E> List<E> list(IntFunction<E> get, IntSupplier size) {
    return mutableIndexList(get, size, Unsupported::exception);
  }

  public static <E> List<E> mutableIndexList(IntFunction<E> get,
                                             IntSupplier size,
                                             IntObjFunction<E> set) {

    return mutableList(get, size, set, Unsupported::exception, Unsupported::exception);
  }

  public static <E> List<E> mutableList(IntFunction<E> get,
                                        IntSupplier size,
                                        IntObjFunction<E> set,
                                        IntFunction<E> remove,
                                        IntObjConsumer<E> add) {
    Objects.requireNonNull(get, "get");
    Objects.requireNonNull(size, "size");
    Objects.requireNonNull(set, "set");
    Objects.requireNonNull(remove, "remove");
    Objects.requireNonNull(add, "add");

    class LambdaList extends AbstractList<E> {

      @Override
      public E get(int index) {
        return get.apply(index);
      }

      @Override
      public int size() {
        return size.getAsInt();
      }

      @Override
      public E set(int index, E element) {
        return set.apply(index, element);
      }

      @Override
      public E remove(int index) {
        return remove.apply(index);
      }

      @Override
      public void add(int index, E e) {
        add.accept(index, e);
      }
    }

    return new LambdaList();
  }
}
