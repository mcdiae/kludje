/*
 * Copyright 2014 McDowell
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.kludje.experimental.test.proxy;

import org.junit.Assert;
import org.junit.Test;
import uk.kludje.experimental.proxy.Interface;

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

  @Test(expected = Error.class)
  @SuppressWarnings("rawtypes")
  public void testRaw() {
    @SuppressWarnings({"unchecked"})
    class BadRawImplementation extends Interface {}
    Object o = new BadRawImplementation().type();
    Assert.fail("Should not reach " + o);
  }

  @Test(expected = Error.class)
  public void testNotInterface() {
    Object o = new Interface<String>() {
    }.type();
    Assert.fail("Should not reach " + o);
  }
}
