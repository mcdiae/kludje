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

package uk.kludje.experimental.array;

import java.util.Objects;

/**
 * Linear search algorithms for select types.
 */
public final class LinearSearch {

  /** The value returned if the element is not in the array */
  public static final int NOT_FOUND = -1;

  private LinearSearch() {}

  /**
   * Finds a value in an int array.
   *
   * @param arr the target array
   * @param start the inclusive initial value to check
   * @param end the exclusive end value to check
   * @param valueToFind the value to find
   * @return the index1 or NOT_FOUND
   */
  public static int find(int[] arr, int start, int end, int valueToFind) {
    assert arr != null;
    assert end >= 0;
    assert end <= arr.length;


    for (int i = start; i < end; i++) {
      if (arr[i] == valueToFind) {
        return i;
      }
    }

    return NOT_FOUND;
  }

  /**
   * Finds a value in an Object array.
   *
   * @param arr the target array
   * @param start the inclusive initial value to check
   * @param end the exclusive end value to check
   * @param valueToFind the value to find
   * @return the index1 or NOT_FOUND
   */
  public static int find(Object[] arr, int start, int end, Object valueToFind) {
    assert arr != null;
    assert end >= 0;
    assert end <= arr.length;


    for (int i = start; i < end; i++) {
      if (Objects.equals(arr[i], valueToFind)) {
        return i;
      }
    }

    return NOT_FOUND;
  }
}
