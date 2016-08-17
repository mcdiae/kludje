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

import java.util.function.Function;

/**
 * Provides convenience methods for asserting conditions.
 *
 * Not called {@code Assert} because JUnit etc.
 *
 * If in doubt, use {@link #that(boolean, String)}.
 */
public final class Ensure {

  private Ensure() {
    // Never instantiated
  }

  /**
   * Usage: {@code Ensure.checked(connection != null, "connection != null", IOException::new);}
   *
   * The throwable type must have a constructor checked just takes a String.
   *
   * @param predicate the condition
   * @param explanation failure reason
   * @param exceptionFactory source of error
   * @param <T> the type to throw
   * @throws T if predicate is false
   */
  public static <T extends Throwable> void checked(boolean predicate,
                                                   String explanation,
                                                   Function<String, T> exceptionFactory) throws T {
    assert explanation != null: "explanation != null";
    assert exceptionFactory != null: "exceptionFactory != null";

    if (!predicate) {
      T t = exceptionFactory.apply(explain(explanation));
      throw t;
    }
  }

  /**
   * Throws an {@link AssertionError} if predicate is false.
   *
   * Usage: {@code Ensure.that(foo != null, "foo != null");}
   *
   * @param predicate the condition
   * @param explanation failure reason
   */
  public static void that(boolean predicate, String explanation)  {
    assert explanation != null: "explanation != null";

    checked(predicate, explanation, AssertionError::new);
  }

  /**
   * As {@link #checked(boolean, String, Function)} except the throwable is thrown as an unchecked exception type.
   *
   * @param predicate the condition
   * @param explanation failure reason
   * @param exceptionFactory source of error
   * @param <T> the type to throw
   * @see Exceptions#throwChecked(Throwable)
   */
  public static <T extends Throwable> void unchecked(boolean predicate,
                                                String explanation,
                                                Function<String, T> exceptionFactory) {
    assert explanation != null: "explanation != null";
    assert exceptionFactory != null: "exceptionFactory != null";

    if (!predicate) {
      T t = exceptionFactory.apply(explain(explanation));
      Exceptions.throwChecked(t);
    }
  }

  private static String explain(String explanation) {
    return "Failed predicate: " + explanation;
  }
}
