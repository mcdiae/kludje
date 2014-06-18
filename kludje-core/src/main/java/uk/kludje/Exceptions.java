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

import java.util.Objects;

/**
 * Methods for translating checked exceptions to unchecked exceptions and back.
 */
public final class Exceptions {
  private Exceptions() {
  }

  /**
   * <p>Throws any type of {@link java.lang.Throwable} as an unchecked type.</p>
   * Usage:
   * <pre>
   *   void foo(Closeable c) {
   *     try {
   *       c.close();
   *     } catch(IOException e) {
   *       Exceptions.throwChecked(e);
   *     }
   *   }
   * </pre>
   *
   * @param t the (non-null) type to throw
   */
  public static UncheckedMarker throwChecked(Throwable t) {
    Objects.requireNonNull(t, "throwable");
    Exceptions.<RuntimeException>throwIt(t);
    return UncheckedMarker.INSTANCE;
  }

  @SuppressWarnings("unchecked")
  private static <T extends Throwable> void throwIt(Throwable t) throws T {
    throw (T) t;
  }

  /**
   * <p>Declares that the scope expects a checked exception.
   * Use this method to catch a checked exception that is not detected by the compiler.</p>
   * <p>
   * Usage:
   * </p>
   * <pre>
   *   void bar();
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
   * </pre>
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
     * It's purpose is to ensure the compiler accepts that T must be handled.
     *
     * @param <T> the type of exception expected
     * @return itself
     * @throws T the exception
     */
    public <T extends Throwable> ExceptionDeclarer expected() throws T {
      return this;
    }
  }

  public static final class UncheckedMarker extends Error {
    private static final UncheckedMarker INSTANCE = new UncheckedMarker();

    private UncheckedMarker() {}
  }
}