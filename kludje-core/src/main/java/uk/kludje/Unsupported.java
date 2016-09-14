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

package uk.kludje;

/**
 * A source of {@link UnsupportedOperationException} method references.
 * For when you just need to pass {@code Unsupported::exception}.
 */
public final class Unsupported {

  private static final String MESSAGE = "A method was called that is not supported by the implementation";

  private Unsupported() {}

  /**
   * @param <R> the generic return type
   * @return never returns
   * @throws UnsupportedOperationException always
   */
  public static <R> R exception() {
    throw new UnsupportedOperationException(MESSAGE);
  }

  /**
   * @param <R> the generic return type
   * @return never returns
   * @throws UnsupportedOperationException always
   */
  public static <R, A> R exception(A a) {
    throw new UnsupportedOperationException(MESSAGE);
  }

  /**
   * @param <R> the generic return type
   * @return never returns
   * @throws UnsupportedOperationException always
   */
  public static <R, A, B> R exception(A a, B b) {
    throw new UnsupportedOperationException(MESSAGE);
  }

  /**
   * @param <R> the generic return type
   * @return never returns
   * @throws UnsupportedOperationException always
   */
  public static <R, A, B, C> R exception(A a, B b, C c) {
    throw new UnsupportedOperationException(MESSAGE);
  }
}
