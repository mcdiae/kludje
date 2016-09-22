package uk.kludje.test;

import org.junit.Test;
import uk.kludje.Fatal;

public class FatalTest {

  @Test(expected = AssertionError.class)
  public void testFatalWhen() {
    Fatal.when(true, "true");
  }

  @Test
  public void testNotFatalWhen() {
    Fatal.when(false, "true");
  }

  @Test(expected = AssertionError.class)
  public void testFatalWhenNull() {
    Fatal.whenNull(null, "null");
  }

  @Test
  public void testNotFatalWhenNull() {
    Fatal.whenNull(new Object(), "null");
  }
}
