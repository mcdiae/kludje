package uk.kludje.proxy;

/**
 * Created by user on 14/06/14.
 */
final class Assert {
  private Assert() {
  }

  public static void ensure(boolean condition, String explanation) {
    if (!condition) {
      throw new AssertionError(explanation);
    }
  }
}
