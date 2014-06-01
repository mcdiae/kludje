package java.co.uk.kludje.test;

import co.uk.kludje.Exceptions;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class ExceptionsTest {

  @Test(expected = IOException.class)
  public void testThrowChecked() {
    Exceptions.throwChecked(new IOException("expected"));
  }

  @Test
  public void testCatch() {
    boolean gotIt = false;
    try {
      Exceptions.throwChecked(new IOException("expected"));
      Exceptions
          .<IOException>expected()
          .<ClassNotFoundException>expected();
    } catch (IOException | ClassNotFoundException e) {
      gotIt = true;
    }
    Assert.assertTrue(gotIt);
  }
}
