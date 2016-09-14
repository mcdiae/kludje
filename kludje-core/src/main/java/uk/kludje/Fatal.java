package uk.kludje;

final class Fatal {

  private Fatal() {}

  public static void when(boolean predicate, String reason) {
    if (predicate) {
      throw new AssertionError("ERROR: " + reason);
    }
  }
}
