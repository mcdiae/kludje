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

final class IntSets {

  private IntSets() {}

  public static IntSet subarrayIntList(int offset, int length, int...array) {
    Assert.that(offset >= 0, "offset >= 0");
    Assert.that(length >= 0, "length >= 0");
    int end = offset + length;
    Assert.that(end >= 0, "offset + length >= 0");
    Assert.that(end <= array.length, "offset + length >= array.length");

    class ArrayIntSet extends AbstractIntSet {

      @Override
      public int size() {
        return length;
      }

      @Override
      public int intAt(int index) {
        return array[offset + index];
      }
    }

    return new ArrayIntSet();
  }

  public static IntSet arrayIntList(int...array) {
    return subarrayIntList(0, array.length, array);
  }
}
