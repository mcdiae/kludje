package uk.kludje.experimental.test.fluent;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import static uk.kludje.experimental.fluent.Mutator.mutate;
import static uk.kludje.fn.function.UConsumer.asUConsumer;

public class MutatorTest {

  @Test
  public void testInvoke() {
    AtomicInteger i = mutate(new AtomicInteger())
        .nil(AtomicInteger::incrementAndGet)
        .nil(AtomicInteger::incrementAndGet)
        .get();
    Assert.assertEquals(2, i.get());
  }

  @Test
  public void testBindInvoke() {
    AtomicInteger i = mutate(new AtomicInteger())
        .nullary(AtomicInteger::incrementAndGet)
        .invoke()
        .invoke()
        .unbind()
        .get();
    Assert.assertEquals(2, i.get());
  }

  @Test(expected = IOException.class)
  public void testExceptions() {
    Consumer<MutatorTest> ti = asUConsumer(MutatorTest::throwIt);
    mutate(this).nil(ti);
  }

  private void throwIt() throws IOException {
    throw new IOException();
  }

  @Test
  public void testListPopulation() {
    List<String> list = mutate(new ArrayList<String>())
        .un(List::add, "a")
        .un(List::add, "b")
        .un(List::add, "c")
        .un(List::remove, "b")
        .map(Collections::unmodifiableList)
        .get();

    Assert.assertEquals(2, list.size());
  }

  @Test
  public void testListPopulationBind() {
    List<String> list = mutate(new ArrayList<String>())
        .<String>unary(List::add)
        .invoke("a")
        .invoke("b")
        .invoke("c")
        .unbind()
        .unary(List::remove)
        .invoke("b")
        .unbind()
        .map(Collections::unmodifiableList)
        .get();

    Assert.assertEquals(2, list.size());
  }

  @Test
  public void testMapPopulation() {
    Map<String, String> map = mutate(new HashMap<String, String>())
        .bi(Map::put, "a", "A")
        .bi(Map::put, "b", "B")
        .bi(Map::put, "c", "C")
        .map(Collections::unmodifiableMap)
        .get();

    Assert.assertEquals(3, map.size());
  }

  @Test
  public void testMapPopulationBind() {
    Map<String, String> map = mutate(new HashMap<String, String>())
        .<String, String>binary(Map::put)
        .invoke("a", "A")
        .invoke("b", "B")
        .invoke("c", "C")
        .get();

    Assert.assertEquals(3, map.size());
  }
}
