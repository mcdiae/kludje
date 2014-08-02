package uk.kludje.test.fn.nary;

import org.junit.Assert;
import org.junit.Test;
import uk.kludje.fluent.FluentBiConsumer;

import java.util.HashMap;
import java.util.Map;

public class FluentBiConsumerTest {

  @Test
  public void test() {
    Map<String, Integer> map = new HashMap<>();
    FluentBiConsumer.fluent(map::put)
        .apply("one", 1)
        .apply("two", 2)
        .apply("three", 3);
    Assert.assertEquals(3, map.size());
  }
}
