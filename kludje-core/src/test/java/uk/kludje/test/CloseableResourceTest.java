package uk.kludje.test;

import org.junit.Assert;
import org.junit.Test;
import uk.kludje.CloseableResource;
import uk.kludje.Exceptions;

import java.util.concurrent.atomic.AtomicBoolean;

public class CloseableResourceTest {

  @Test
  public void compileTest() {
    try(CloseableResource res = this::noop) {
    }
  }

  @Test
  public void exceptionTest() {
    AtomicBoolean gotit = new AtomicBoolean();
    try(CloseableResource res = throwCre().<CRE>expected()) {
    } catch (CRE e) {
      gotit.set(true);
    }
    Assert.assertTrue(gotit.get());
  }

  private CloseableResource throwCre() {
    return () -> Exceptions.throwChecked(new CRE());
  }

  private void noop() {}

  private static class CRE extends Exception {
  }
}
