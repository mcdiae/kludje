package uk.kludje.fluent.test;

import org.junit.Assert;
import org.junit.Test;
import uk.kludje.fluent.Fluent;

import java.util.concurrent.atomic.AtomicInteger;

public class FluentTest {

  @Test
  public void testInvoke() {
    AtomicInteger i = Fluent.fluent(new AtomicInteger())
        .run(AtomicInteger::incrementAndGet)
        .run(AtomicInteger::incrementAndGet)
        .get();
    Assert.assertEquals(2, i.get());
  }
}
