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

import java.util.function.IntSupplier;
import java.util.function.IntUnaryOperator;

public final class IntSequences {

  private static final IntSequence EMPTY = arrayIntList();

  private IntSequences() {}

  public static IntSequence subarrayIntList(int offset, int length, int...array) {
    Assert.that(offset >= 0, "offset >= 0");
    Assert.that(length >= 0, "length >= 0");
    int end = offset + length;
    Assert.that(end >= 0, "offset + length >= 0");
    Assert.that(end <= array.length, "offset + length >= array.length");

    class ArrayIntSequence extends AbstractIntSequence {

      @Override
      public int size() {
        return length;
      }

      @Override
      public int intAt(int index) {
        return array[offset + index];
      }
    }

    return new ArrayIntSequence();
  }

  public static IntSequence arrayIntList(int...array) {
    return subarrayIntList(0, array.length, array);
  }

  public static IntSequence emptyIntList() {
    return EMPTY;
  }

  public static IntSequence intSequence(IntSupplier size, IntUnaryOperator intAt) {
    class LambdaIntSequence extends AbstractIntSequence {

      @Override
      public int size() {
        return size.getAsInt();
      }

      @Override
      public int intAt(int index) {
        return intAt.applyAsInt(index);
      }
    }

    return new LambdaIntSequence();
  }
}
