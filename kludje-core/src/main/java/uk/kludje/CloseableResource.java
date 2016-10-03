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

/**
 * <p>An {@link java.lang.AutoCloseable} type without a throws declaration.</p>
 *
 * <p>For use in try-with-resources blocks. Example usage:</p>
 *
 * <pre><code>
 *  SomeResource r = new SomeResource();
 *  try (CloseableResource closeable = r::release) {
 *    // use r here
 *  }
 * </code></pre>
 */
@FunctionalInterface
public interface CloseableResource extends AutoCloseable {

  /**
   * Overrides the parent method which throws {@link java.lang.Exception}.
   * Combined with {@link #expected()} this type can declare checked it throws any type.
   */
  @Override
  void close();

  /**
   * Fluent method for allowing an instance to declare it throws any {@code Throwable}.
   *
   * @param <T> the type of exception to expect
   * @return this
   * @see Exceptions#expected()
   */
  default <T extends Throwable> CloseableResource expected() throws T {
    return this;
  }
}
