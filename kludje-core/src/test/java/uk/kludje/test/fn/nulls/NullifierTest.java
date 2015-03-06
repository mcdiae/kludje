/*
 * Copyright 2015 McDowell
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

package uk.kludje.test.fn.nulls;

import org.junit.Assert;
import org.junit.Test;
import uk.kludje.fn.nulls.Nullifier;

public class NullifierTest {

  @Test
  public void testNullHandling() {
    Nullifier<String, String> nullifier = s -> s.concat(s);

    // not null
    String str = nullifier.apply("foo");
    Assert.assertEquals("foofoo", str);

    // null
    String nullStr = nullifier.apply(null);
    Assert.assertNull(nullStr);
  }

  @Test
  public void testChain() {
    // setup
    A a = new A();
    a.b = new B();
    a.b.c = new C();
    a.b.c.d = new D();
    // invoke
    D d = Nullifier.check(A::getB, B::getC, C::getD).apply(a);
    // verify
    Assert.assertEquals(a.b.c.d, d);
  }

  @Test
  public void testNullChain() {
    // setup
    A a = new A();
    // invoke
    D d = Nullifier.check(A::getB, B::getC, C::getD).apply(a);
    // verify
    Assert.assertNull(d);
  }

  @Test
  public void testPartialChain() {
    // setup
    A a = new A();
    a.b = new B();
    // invoke
    D d = Nullifier.check(A::getB, B::getC, C::getD).apply(a);
    // verify
    Assert.assertNull(d);
  }

  class A {
    B b;
    B getB() {
      return b;
    }
  }

  class B {
    C c;
    C getC() {
      return c;
    }
  }

  class C {
    D d;
    D getD() {
      return d;
    }
  }

  class D {}
}
