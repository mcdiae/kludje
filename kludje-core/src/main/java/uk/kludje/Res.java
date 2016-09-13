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
    Fatal.when(closeFunction == null, "closeFunction == null");
    Fatal.when(resource == null, "resource == null");

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
      Exceptions.throwChecked(e);
    }
  }

  /**
   * Creates a new {@link Res} instance.
   *
   * @param closeFunction the function for releasing the resource
   * @param resource the resource instance
   * @param <R> the resource type
   * @return
   */
  public static <R> Res<R> res(CloseFunction<R> closeFunction, R resource) {
    return new Res<>(closeFunction, resource);
  }

  @Override
  public <T extends Throwable> Res<R> expected() throws T {
    return this;
  }

  /**
   * @param <R> the resource type
   */
  @FunctionalInterface
  public interface CloseFunction<R> {

    /**
     * @param resource the resource to close
     * @throws Exception on error
     */
    void close(R resource) throws Exception;
  }
}
