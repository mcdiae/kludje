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

import java.util.*;
import java.util.function.ToIntFunction;

public final class ListAdapterFactory<S, E> {
  private final IndexOp<S, E> get;
  private final ToIntFunction<S> size;
  private final IndexValueOp<S, E> set;
  private final IndexValueOp<S, E> add;
  private final IndexOp<S, E> remove;

  private ListAdapterFactory(IndexOp<S, E> get,
                             ToIntFunction<S> size,
                             IndexValueOp<S, E> set,
                             IndexValueOp<S, E> add,
                             IndexOp<S, E> remove) {
    this.get = get;
    this.size = size;
    this.set = set;
    this.add = add;
    this.remove = remove;
  }

  public List<E> adapt(S s) {
    class ListAdapter extends AbstractList<E> {

      @Override
      public E get(int index) {
        return ListAdapterFactory.this.get.get(s, index);
      }

      @Override
      public int size() {
        return ListAdapterFactory.this.size.applyAsInt(s);
      }
    }

    return new ListAdapter();
  }

  public ListAdapterFactory<S, E> modifiable(IndexValueOp<S, E> set) {
   return new ListAdapterFactory<>(this.get, this.size, set, this.add, this.remove);
  }

  public ListAdapterFactory<S, E> expandable(IndexValueOp<S, E> add, IndexOp<S, E> remove) {
    if(set == defaultIndexValueOp()) {
      throw new IllegalStateException("must call modifiable before invoking expandable");
    }
    return new ListAdapterFactory<S, E>(this.get, this.size, this.set, add, remove);
  }

  private static IndexValueOp<Object, Object> DEFAULT_INDEX_VALUE_OP =
      (src, idx, val) -> { throw new UnsupportedOperationException(); };

  private static <S, E> IndexValueOp<S, E> defaultIndexValueOp() {
    @SuppressWarnings("unchecked")
    IndexValueOp<S, E> op = (IndexValueOp<S, E>) DEFAULT_INDEX_VALUE_OP;
    return op;
  }

  private static IndexOp<Object, Object> DEFAULT_INDEX_OP =
      (src, idx) -> { throw new UnsupportedOperationException(); };

  private static <S, E> IndexOp<S, E> defaultIndexOp() {
    @SuppressWarnings("unchecked")
    IndexOp<S, E> op = (IndexOp<S, E>) DEFAULT_INDEX_OP;
    return op;
  }

  public static <S, E> ListAdapterFactory<S, E> listAdapterFactory(IndexOp<S, E> get,
                                                                   ToIntFunction<S> size) {
    return new ListAdapterFactory(get, size, defaultIndexValueOp(), defaultIndexValueOp(), defaultIndexOp());
  }

  public static interface IndexOp<SOURCE, ELEMENT> {
    ELEMENT get(SOURCE from, int index);
  }

  public static interface IndexValueOp<SOURCE, ELEMENT> {
    ELEMENT get(SOURCE from, int index, ELEMENT value);
  }
}
