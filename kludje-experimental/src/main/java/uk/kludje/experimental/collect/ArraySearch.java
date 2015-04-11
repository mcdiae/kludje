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


import java.util.Objects;

final class ArraySearch {

  private ArraySearch() {
  }

  public static int linearSearch(int[] arr, int limit, int value) {
    assert arr != null;
    assert limit >= 0;
    assert limit <= arr.length;

    int found = -1;

    for (int i = 0; i < limit; i++) {
      if (arr[i] == value) {
        return i;
      }
    }

    return found;
  }

  public static int linearSearch(Object[] arr, int limit, Object value) {
    assert arr != null;
    assert limit >= 0;
    assert limit <= arr.length;

    int found = -1;

    for (int i = 0; i < limit; i++) {
      if (Objects.equals(arr[i], value)) {
        return i;
      }
    }

    return found;
  }
}
