/*
 * Copyright 2016 McDowell
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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * For performing simple method argument validation.
 * If this type raises an error it indicates that an API has been used incorrectly.
 */
public final class Fatal {

  private static final Logger LOG = Logger.getLogger(Fatal.class.getName());

  private Fatal() {}

  /**
   * <p>Example: {@code Fatal.when(foo == null, "foo == null");}</p>
   * <p>Error message: "ERROR: foo == null"</p>
   *
   * @param predicate true throws an error
   * @param reason a textual version of the error
   * @throws AssertionError when predicate is true
   */
  public static void when(boolean predicate, String reason) {
    when(predicate, reason, rsn -> rsn, AssertionError::new);
  }

  /**
   * <p>Example: {@code Fatal.whenNull(foo, "foo");}</p>
   * <p>When {@code foo} is null an error will be thrown.</p>
   *
   * @param variable the variable
   * @param variableName the variable name (should match documented name)
   * @throws AssertionError when variable is null
   */
  public static void whenNull(Object variable, String variableName) {
    when(variable == null, variableName, vn -> vn + " == null", AssertionError::new);
  }

  private static <T extends Throwable> void when(boolean predicate, String reasonSource, Function<String, String> reasonFunction, Function<String, T> throwableFactory) {
    if (predicate) {
      String message = "ERROR: " + reasonFunction.apply(reasonSource);
      Throwable t = throwableFactory.apply(message);

      LOG.log(Level.SEVERE, message, t);

      throw Exceptions.throwChecked(t);
    }
  }
}
