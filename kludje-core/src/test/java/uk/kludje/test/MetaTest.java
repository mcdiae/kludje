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

package uk.kludje.test;

import org.junit.Assert;
import org.junit.Test;
import uk.kludje.Meta;
import uk.kludje.MetaConfig;
import uk.kludje.property.Getter;
import uk.kludje.property.PropertyType;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class MetaTest {

  @Test(expected = Error.class)
  public void testNoNullClass() {
    Meta.meta(null);
  }

  @Test
  public void testBasicEquality() {
    MetaPojo pojo = new MetaPojo();
    Assert.assertTrue(pojo.equals(pojo));
    Assert.assertFalse(pojo.equals(null));
    Assert.assertFalse(pojo.equals(new Object()));
  }

  @Test
  public void basicTest() {
    Assert.assertEquals(new MetaPojo(), new MetaPojo());
    Assert.assertEquals(new MetaPojo().hashCode(), new MetaPojo().hashCode());
    Assert.assertEquals(new MetaPojo().toString(), new MetaPojo().toString());
  }

  @Test
  public void comboTest() {
    AtomicInteger count = new AtomicInteger();

    Stream.<Consumer<MetaPojo>>of(
        m -> m.a = true,
        m -> m.b = 'a',
        m -> m.c = -1,
        m -> m.d = -2,
        m -> m.e = 10,
        m -> m.f = 5L,
        m -> m.g = 1.0f,
        m -> m.h = new Object(),
        m -> m.i = "",
        m -> m.j = 1.0
    ).forEach(c -> {
      MetaPojo pojo = new MetaPojo();
      c.accept(pojo);

      Assert.assertNotEquals("hint=" + count.getAndIncrement(), pojo, new MetaPojo());
      Assert.assertNotEquals(pojo.toString(), new MetaPojo().toString());
    });
  }

  @Test
  public void testSubclass() {
    MetaPojo base = new MetaPojo();
    MetaPojo sub = new MetaPojo() {};

    Assert.assertEquals(base, sub);
    Assert.assertEquals(sub, base);
  }

  @Test(expected = NullPointerException.class)
  public void testBadGetter() {
    Getter<MetaPojo> badGetter = new Getter<MetaPojo>() {
      @Override
      public Object get(MetaPojo metaPojo) {
        return null;
      }

      @Override
      public PropertyType type() {
        return null;
      }
    };

    Meta<MetaPojo> meta = Meta.<MetaPojo>meta()
      .objects(badGetter);

    meta.toString(new MetaPojo());
  }

  @Test(expected = Error.class)
  public void testNullConfig() {
    Meta.meta(Object.class).configure(null);
  }

  @Test(expected = Error.class)
  public void testGuardedAgainstNullGetter() {
    Meta.meta(MetaPojo.class)
      .objects(pojo -> pojo.a, null);
  }

  @Test(expected = Error.class)
  public void testGuardedAgainstNullName() {
    Meta.meta(MetaPojo.class)
      .namedObject(null, pojo -> pojo.a);
  }

  private static final Meta<MetaPojo> META = Meta.meta(MetaPojo.class)
      .booleans($ -> $.a)
      .chars($ -> $.b)
      .bytes($ -> $.c)
      .shorts($ -> $.d)
      .ints($ -> $.e)
      .longs($ -> $.f)
      .floats($ -> $.g)
      .objects($ -> $.h, $ -> $.i)
      .doubles($ -> $.j)
      .configure(MetaConfig.defaultConfig().withInstanceofEqualsTypeCheck());

  private static class MetaPojo {
    boolean a;
    char b;
    byte c;
    short d;
    int e;
    long f;
    float g;
    Object h;
    String i;
    double j;

    @Override
    public boolean equals(Object obj) {
      return META.equals(this, obj);
    }

    @Override
    public int hashCode() {
      return META.hashCode(this);
    }

    @Override
    public String toString() {
      return META.toString(this);
    }
  }
}
