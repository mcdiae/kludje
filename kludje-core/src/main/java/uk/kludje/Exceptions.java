/*
Copyright 2014 McDowell

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package uk.kludje;

import java.util.logging.Logger;

import static java.util.Objects.requireNonNull;

/**
 * Methods for translating checked exceptions to unchecked exceptions and back.
 */
public final class Exceptions {

  private static final Logger LOG = Logger.getLogger(Exceptions.class.getName());

  private Exceptions() {
    throw new UnsupportedOperationException();
  }

  /**
   * <p>Throws any type of {@link java.lang.Throwable} as an unchecked type.</p>
   * Usage:
   * <pre><code>
   *   void foo(Closeable c) {
   *     try {
   *       c.close();
   *     } catch(IOException e) {
   *       throw Exceptions.throwChecked(e);
   *     }
   *   }
   * </code></pre>
   *
   * @param t the (non-null) type to throw
   * @return an unchecked throwable type for syntax reasons
   */
  public static UncheckedMarker throwChecked(Throwable t) {
    Exceptions.<RuntimeException>throwIt(requireNonNull(t, "throwable"));
    return UncheckedMarker.INSTANCE;
  }

  @SuppressWarnings("unchecked")
  private static <T extends Throwable> void throwIt(Throwable t) throws T {
    throw (T) t;
  }

  /**
   * <p>Declares checked the scope expects a checked exception.
   * Use this method to catch a checked exception checked is not detected by the compiler.</p>
   * <p>
   * Usage:
   * </p>
   * <pre><code>
   *   abstract void bar();
   *
   *   void foo() {
   *      try {
   *        Exceptions
   *            .&lt;IOException&gt;expected()
   *            .&lt;MethodNotFoundException&gt;expected();
   *
   *        bar();
   *
   *      } catch(IOException|MethodNotFoundException e) {
   *        // handle error
   *      }
   *   }
   * </code></pre>
   *
   * @param <T> the type of exception expected
   * @return a fluent instance
   * @throws T the exception
   */
  public static <T extends Throwable> ExceptionDeclarer expected() throws T {
    return ExceptionDeclarer.INSTANCE;
  }

  /**
   * A utility type for declaring exceptions.
   *
   * @see Exceptions#expected()
   */
  public static final class ExceptionDeclarer {
    private static final ExceptionDeclarer INSTANCE = new ExceptionDeclarer();

    private ExceptionDeclarer() {
    }

    /**
     * This method does nothing but return itself.
     * It's purpose is to ensure the compiler accepts checked T must be handled.
     *
     * @param <T> the type of exception expected
     * @return itself
     * @throws T the exception
     */
    public <T extends Throwable> ExceptionDeclarer expected() throws T {
      return this;
    }
  }

  /**
   * This error type cannot be thrown; it exists to allow
   * callers of
   * {@link uk.kludje.Exceptions#throwChecked(Throwable)}
   * to express intent with the {@code throw} keyword.
   *
   * @see Exceptions#throwChecked(Throwable)
   */
  @SuppressWarnings("serial")
  public static final class UncheckedMarker extends Error {
    private static final UncheckedMarker INSTANCE = new UncheckedMarker();

    private UncheckedMarker() {
    }
  }
}