package uk.kludje.test;

import org.junit.Assert;
import org.junit.Test;
import uk.kludje.Res;

import static uk.kludje.Res.res;

public class ResTest {

  @Test
  public void testRelease() {
    TestResource tr = new TestResource();
    try (Res<TestResource> testResource = res(TestResource::release, tr).<TestException>expected()) {
      testResource.unwrap().invoke();
    } catch (TestException e) {
      throw new AssertionError(e);
    }
    // verify
    Assert.assertTrue(tr.closed);
    Assert.assertTrue(tr.invoked);
  }

  @SuppressWarnings("try")
  @Test(expected = TestException.class)
  public void testThrow() {
    TestResource tr = new TestResource() {
      @Override
      public void release() throws TestException {
        throw new TestException();
      }
    };
    // invoke
    try (Res<TestResource> testResource = res(TestResource::release, tr)) {}
  }

  @Test(expected = Error.class)
  public void testThrowsOnNullFunction() {
    Res.res(null, new TestResource());
  }

  @Test(expected = Error.class)
  public void testThrowsOnNullResource() {
    Res.CloseFunction<Object> closeFunction = o -> {};
    Res.res(closeFunction, null);
  }

  private static class TestResource {
    private boolean closed;
    private boolean invoked;

    public void release() throws TestException {
      closed = true;
    }

    void invoke() {
      invoked = true;
    }
  }

  @SuppressWarnings("serial")
  private static class TestException extends Exception {
  }
}
