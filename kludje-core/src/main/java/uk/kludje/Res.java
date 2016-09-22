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

/**
 * Adapts any type to {@link AutoCloseable} so that its instances can be closed by try blocks.
 *
 * Usage:
 *
 * <pre><code>
 *   try (Res&lt;SomeResource> someResource = Res.res(SomeResource::release, new SomeResource)) {
 *     someResource.unwrap().foo();
 *   }
 * </code></pre>
 *
 * @param <R> the underlying resource type
 */
public class Res<R> implements CloseableResource {

  private final CloseFunction<R> closeFunction;
  private final R resource;

  private Res(CloseFunction<R> closeFunction, R resource) {
    Fatal.whenNull(closeFunction, "closeFunction");
    Fatal.whenNull(resource, "resource");

    this.closeFunction = closeFunction;
    this.resource = resource;
  }

  /**
   * @return the resource instance
   */
  public R unwrap() {
    return resource;
  }

  /**
   * Throws any exception thrown by {@link CloseFunction#close(Object)}
   * including checked exceptions.
   *
   * If the {@link CloseFunction} throws a checked exception not declared by any other call use {@link #expected()}
   * to tell the compiler to expect it.
   */
  @Override
  public void close() {
    try {
      closeFunction.close(resource);
    } catch (Exception e) {
      throw Exceptions.throwChecked(e);
    }
  }

  /**
   * Creates a new {@link Res} instance.
   *
   * @param closeFunction the function for releasing the resource
   * @param resource the resource instance
   * @param <R> the resource type
   * @return a new instance
   */
  public static <R> Res<R> res(CloseFunction<R> closeFunction, R resource) {
    return new Res<>(closeFunction, resource);
  }

  @Override
  public <T extends Throwable> Res<R> expected() throws T {
    return this;
  }

  /**
   * Functional interface for closing a resource.
   *
   * @param <R> the resource type
   * @see Res#res(CloseFunction, Object)
   */
  @FunctionalInterface
  public interface CloseFunction<R> {

    /**
     * Releases the resource argument.
     *
     * @param resource the resource to close
     * @throws Exception on error
     */
    void close(R resource) throws Exception;
  }
}
