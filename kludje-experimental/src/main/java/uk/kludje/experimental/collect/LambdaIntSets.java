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

import java.util.function.Supplier;
import java.util.stream.IntStream;

/**
 * Provides methods for creating {@link IntSet} instances.
 */
public final class LambdaIntSets {

  private static final IntSet EMPTY = lambdaIntSet(() -> IntStream.empty());

  private LambdaIntSets() {}

  /**
   * The empty set.
   *
   * @return an immutable empty set
   */
  public static IntSet emptyIntSet() {
    return EMPTY;
  }

  /**
   * Creates an {@link IntSet} from a stream supplier.
   * The argument implements the {@link IntSet#stream()} method.
   *
   * @param stream implements the stream() method
   * @return a new instance
   */
  public static IntSet lambdaIntSet(Supplier<IntStream> stream) {
    assert stream != null;

    class LambdaIntSet extends AbstractIntSet {

      @Override
      public IntStream stream() {
        return stream.get();
      }
    }

    return new LambdaIntSet();
  }

  public static IntSet toArrayIntSet(int[] ints, int offset, int size) {
    assert (offset >= 0) && (offset < ints.length);
    assert size <= (ints.length - offset);

    int[] backer = new int[ints.length];
    int count = 0;
    int max = offset + size;
    for (int idx = offset; idx < max; idx++) {
      int current = ints[idx];
      if (ArraySearch.linearSearch(backer, count, current) == ArraySearch.NOT_FOUND) {
        backer[count++] = current;
      }
    }
    int setSize = count;

    return lambdaIntSet(() -> IntStream.of(backer).limit(setSize));
  }

  public static IntSet intSetOf(int... ints) {
    return toArrayIntSet(ints, 0, ints.length);
  }
}
