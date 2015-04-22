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

public final class SparseArrays {
  private SparseArrays() {}

  public static <V> SparseArray<V> linearSparseArray(int increment) {
    return new LinearSparseArray<>(increment);
  }

  public static <V> SparseArray<V> sparseArray() {
    return new LinearSparseArray<>(10);
  }
}
