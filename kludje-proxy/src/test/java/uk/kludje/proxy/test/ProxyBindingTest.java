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

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import uk.kludje.proxy.Interface;
import uk.kludje.fn.function.UFunction;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

import static uk.kludje.proxy.ProxyBinding.binder;
import static uk.kludje.proxy.ProxyBinding.proxy;

public class ProxyBindingTest {

  private static final String TEST = "Hello, World!";

  @Test
  public void testProxyBinding() {
    AtomicReference<String> result = new AtomicReference<>();
    Runnable action = () -> {
      result.set(TEST);
    };

    Runnable r = binder(Runnable.class, Runnable.class)
        .bind(proxy(Runnable.class)::run, action::run);
    r.run();
    Assert.assertEquals(TEST, result.get());
  }

  @Test
  public void testMultiBinding() throws SQLException {
    AtomicBoolean closed = new AtomicBoolean();
    AutoCloseable autoCloseable = () -> closed.set(true);
    Function<String, String> fn = (i) -> Objects.requireNonNull(i, "test");

    ResultSet resultSet = proxy(ResultSet.class);
    binder(ResultSet.class, AutoCloseable.class)
      .bind(resultSet::close, autoCloseable::close);
    binder(ResultSet.class, new Interface<UFunction<String, String>>() {})
        .bind(resultSet::getString, fn::apply);

    String actual = resultSet.getString(TEST);
    resultSet.close();

    Assert.assertEquals(TEST, actual);
    Assert.assertTrue(closed.get());
  }

  /**
   * broken
   */
  @Ignore
  @Test
  public void testAutoboxing() {
    int expected = 10;
    IntSupplier intSupplier = () -> expected;
    Class<Supplier<Integer>> TYPE = new Interface<Supplier<Integer>>() {
    }.type();
    Supplier<Integer> supplier = binder(TYPE, IntSupplier.class).bind(proxy(TYPE)::get, intSupplier::getAsInt);

    int actual = supplier.get();

    Assert.assertEquals(expected, actual);
  }

  /**
   * Exception propagation currently broken.
   */
  @Ignore
  @Test(expected = UncheckedIOException.class)
  public void testExceptions() {
    Runnable action = () -> {
      throw new UncheckedIOException(new IOException());
    };

    Runnable r = binder(Runnable.class, Runnable.class).bind(proxy(Runnable.class)::run, action::run);
    r.run();
  }
}
