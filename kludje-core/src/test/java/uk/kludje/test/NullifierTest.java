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

package uk.kludje.test;

import org.junit.Assert;
import org.junit.Test;
import uk.kludje.Nullifier;

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
  public void testSpan() {
    // setup
    A a = new A();
    a.b = new B();
    a.b.c = new C();
    a.b.c.d = new D();
    // invoke
    D d = Nullifier.span(A::getB, B::getC, C::getD).apply(a);
    // verify
    Assert.assertEquals(a.b.c.d, d);
  }

  @Test
  public void testNullSpan() {
    // setup
    A a = new A();
    // invoke
    D d = Nullifier.span(A::getB, B::getC, C::getD).apply(a);
    // verify
    Assert.assertNull(d);
  }

  @Test
  public void testPartialSpan() {
    // setup
    A a = new A();
    a.b = new B();
    // invoke
    D d = Nullifier.span(A::getB, B::getC, C::getD).apply(a);
    // verify
    Assert.assertNull(d);
  }

  @Test
  public void testSpan2() {
    // setup
    A a = new A();
    a.b = new B();
    a.b.c = new C();
    a.b.c.d = new D();
    // invoke
    C c = Nullifier.span(A::getB, B::getC).apply(a);
    // verify
    Assert.assertEquals(a.b.c, c);
  }

  @Test
  public void testSpan4() {
    // setup
    A a = new A();
    a.b = new B();
    a.b.c = new C();
    a.b.c.d = new D();
    a.b.c.d.e = new E();
    // invoke
    E e = Nullifier.span(A::getB, B::getC, C::getD, D::getE).apply(a);
    // verify
    Assert.assertEquals(a.b.c.d.e, e);
  }

  @Test
  public void testIsNullSpan() {
    // setup
    A a = new A();
    a.b = new B();
    a.b.c = new C();
    a.b.c.d = new D();
    // invoke
    boolean state = Nullifier.isNull(a, A::getB, B::getC, C::getD);
    // verify
    Assert.assertFalse(state);
  }

  @Test
  public void testIsNull() {
    // invoke
    boolean state = Nullifier.isNull(null, A::getB, B::getC, C::getD);
    // verify
    Assert.assertTrue(state);
  }

  @Test
  public void testIsPartialNull() {
    // setup
    A a = new A();
    a.b = new B();
    // invoke
    boolean state = Nullifier.isNull(a, A::getB, B::getC, C::getD);
    // verify
    Assert.assertTrue(state);
  }

  @Test
  public void testNull1() {
    // setup
    A a = new A();
    a.b = new B();
    // invoke
    boolean state = Nullifier.isNull(a, A::getB);
    // verify
    Assert.assertFalse(state);
  }

  @Test
  public void testNull2() {
    // setup
    A a = new A();
    a.b = new B();
    a.b.c = new C();
    a.b.c.d = new D();
    // invoke
    boolean state = Nullifier.isNull(a, A::getB, B::getC);
    // verify
    Assert.assertFalse(state);
  }

  @Test
  public void testNull4() {
    // setup
    A a = new A();
    a.b = new B();
    a.b.c = new C();
    a.b.c.d = new D();
    a.b.c.d.e = new E();
    // invoke
    boolean state = Nullifier.isNull(a, A::getB, B::getC, C::getD, D::getE);
    // verify
    Assert.assertFalse(state);
  }

  /** Inheritance tests check for generics compilation problems. */
  @Test
  public void testInheritanceCompilation2() {
    // setup
    Nullifier<String, CharSequence> nullifier = Nullifier.span(this::toCharSequence,
        this::toCharSequence);
    // invoke
    CharSequence foo = nullifier.apply("foo");
    // verify
    Assert.assertEquals("foo", foo);
  }

  @Test
  public void testInheritanceCompilation3() {
    // setup
    Nullifier<String, CharSequence> nullifier = Nullifier.span(this::toCharSequence,
        this::toCharSequence,
        this::toCharSequence);
    // invoke
    CharSequence foo = nullifier.apply("foo");
    // verify
    Assert.assertEquals("foo", foo);
  }

  @Test
  public void testInheritanceCompilation4() {
    // setup
    Nullifier<String, CharSequence> nullifier = Nullifier.span(this::toCharSequence,
        this::toCharSequence,
        this::toCharSequence,
        this::toCharSequence);
    // invoke
    CharSequence foo = nullifier.apply("foo");
    // verify
    Assert.assertEquals("foo", foo);
  }

  @Test
  public void testOrElseSpan() {
    // setup
    A a = new A();
    D expected = new D();
    // invoke
    D d = Nullifier.span(A::getB, B::getC, C::getD).applyOr(a, expected);
    // verify
    Assert.assertEquals(expected, d);
  }

  @Test
  public void testOrElseGetSpan() {
    // setup
    A a = new A();
    // invoke
    D d = Nullifier.span(A::getB, B::getC, C::getD).applyOrGet(a, D::new);
    // verify
    Assert.assertNotNull(d);
  }

  private CharSequence toCharSequence(Object cs) {
    return cs.toString();
  }

  static class A {
    B b;
    B getB() {
      return b;
    }
  }

  static class B {
    C c;
    C getC() {
      return c;
    }
  }

  static class C {
    D d;
    D getD() {
      return d;
    }
  }

  static class D {
    E e;
    E getE() {
      return e;
    }
  }

  static class E {}
}
