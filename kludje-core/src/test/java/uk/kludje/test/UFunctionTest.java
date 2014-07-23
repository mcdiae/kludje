package uk.kludje.test;

import org.junit.Assert;
import org.junit.Test;

import java.util.function.Function;

public class UFunctionTest {
  private static final String EXPECTED = "ok";

  @Test
  public void testOk() {
    Function<String, String> fn = (UFunction<String, String>) this::pass;
    String actual = fn.apply(EXPECTED);
    Assert.assertEquals(EXPECTED, actual);
  }

  private String pass(String str) throws Exception {
    return str;
  }

  @Test(expected = Exception.class)
  public void testFail() {
    Function<String, String> fn = (UFunction<String, String>) s -> {
      throw new Exception();
    };
    fn.apply(EXPECTED);
  }
}
