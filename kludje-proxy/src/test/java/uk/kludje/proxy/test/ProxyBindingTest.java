/*
Copyright 2014 McDowell

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package uk.kludje.proxy.test;

import junit.framework.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.concurrent.atomic.AtomicReference;

import static uk.kludje.proxy.ProxyBinding.binder;
import static uk.kludje.proxy.ProxyBinding.proxy;

@Ignore
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

  @Test(expected = UncheckedIOException.class)
  public void testExceptions() {
    Runnable action = () -> {
      throw new UncheckedIOException(new IOException());
    };

    Runnable r = binder(Runnable.class, Runnable.class).bind(proxy(Runnable.class)::run, action::run);
    r.run();
  }
}
