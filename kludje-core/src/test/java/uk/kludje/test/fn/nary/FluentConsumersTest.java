package uk.kludje.test.fn.nary;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static uk.kludje.fluent.FluentConsumer.fluent;

public class FluentConsumersTest {

  @Test
  public void testFluentConsumer() {
    List<Integer> a = new ArrayList<>();
    Consumer<Integer> consumer = a::add;
    fluent(consumer).apply(1)
        .apply(2);
    Assert.assertEquals(2, a.size());
  }
}
