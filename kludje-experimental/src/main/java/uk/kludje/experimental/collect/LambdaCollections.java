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

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.Function;


public final class LambdaCollections {

  private LambdaCollections() {}

  public static <E, R> Collection<R> mapped(Collection<E> data,
                                                Function<? super E, R> mapper) {
    Objects.requireNonNull(data, "data");
    Objects.requireNonNull(mapper, "mapper");

    class LambdaCollection extends AbstractCollection<R> {

      @Override
      public Iterator<R> iterator() {
        Iterator<E> it = data.iterator();
        return LambdaIterators.iterator(it::hasNext, () -> {
          E value = it.next();
          return mapper.apply(value);
        });
      }

      @Override
      public int size() {
        return data.size();
      }
    }

    return new LambdaCollection();
  }
}
