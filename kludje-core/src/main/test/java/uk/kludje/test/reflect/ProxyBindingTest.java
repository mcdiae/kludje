package uk.kludje.test.reflect;

import junit.framework.Assert;
import org.junit.Test;
import uk.kludje.Exceptions;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import static uk.kludje.experimental.ProxyBinding.binder;
import static uk.kludje.experimental.ProxyBinding.proxy;

public class ProxyBindingTest {

  private static final String TEST = "Hello, World!";

  @Test
  public void testProxyBinding() {
    AtomicReference<String> result = new AtomicReference<>();
    Runnable action = () -> {
      result.set(TEST);
    };

    Runnable r = binder(Runnable.class, Runnable.class).bind(proxy(Runnable.class)::run, action::run);
    r.run();
    Assert.assertEquals(TEST, result.get());
  }

  @Test(expected = IOException.class)
  public void testExceptions() {
    Runnable action = () -> {
      Exceptions.throwChecked(new IOException());
    };

    Runnable r = binder(Runnable.class, Runnable.class).bind(proxy(Runnable.class)::run, action::run);
    r.run();
  }
}
