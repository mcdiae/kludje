package uk.kludje.proxy.test;

import org.junit.Assert;
import org.junit.Test;
import uk.kludje.proxy.Interface;

import java.util.List;
import java.util.function.Supplier;

public class InterfaceTest {

  @Test
  public void testSimple() {
    Interface<Runnable> iface = Interface.type(Runnable.class);
    Assert.assertTrue(iface.type() == Runnable.class);
  }

  @Test
  public void testSimpleGenerics() {
    Interface<Runnable> iface = new Interface<Runnable>() {
    };
    Assert.assertEquals(Runnable.class, iface.type());
  }

  @Test
  public void testGenerified() {
    Object iface = new Interface<Supplier<Runnable>>() {
    }.type();
    Assert.assertEquals(Supplier.class, iface);
  }

  @Test
  public void testDeepGenerics() {
    Object iface = new Interface<List<Supplier<Runnable>>>() {
    }.type();
    Assert.assertEquals(List.class, iface);
  }

  @Test
  public void testGenericsWithWildCard() {
    Object iface = new Interface<List<?>>() {
    }.type();
    Assert.assertEquals(List.class, iface);
  }

  @Test(expected = Interface.IllegalImplementationError.class)
  public void testRaw() {
    @SuppressWarnings({"unchecked", "unused"})
    Object raw = new Interface() {
    }.type();
  }

  @Test(expected = Interface.IllegalImplementationError.class)
  public void testNotInterface() {
    @SuppressWarnings("unused")
    Object raw = new Interface<String>() {
    }.type();
  }
}
