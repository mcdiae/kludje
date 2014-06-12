package uk.kludje;

final class Asserts {
  private Asserts() {}

  public static <T> T notNull(T t, String what) {
    if(t == null) {
      throw new AssertionError(what + " is null");
    }
    return t;
  }
}
