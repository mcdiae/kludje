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
import org.junit.Before;
import org.junit.Test;
import uk.kludje.Meta;

import static java.util.Arrays.asList;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class MetaNamedTest {

  @Test
  public void basicTest() {
    Assert.assertEquals(new MetaPojo(), new MetaPojo());
    Assert.assertEquals(new MetaPojo().hashCode(), new MetaPojo().hashCode());
    Assert.assertEquals(new MetaPojo().toString(), new MetaPojo().toString());
  }

  @Test
  public void comboTest() {
    Arrays.<Consumer<MetaPojo>>asList(
      m -> m.a = true,
      m -> m.b = 'a',
      m -> m.c = -1,
      m -> m.d = -2,
      m -> m.e = 10,
      m -> m.f = 5l,
      m -> m.g = 1.0f,
      m -> m.h = new Object(),
      m -> m.i = "",
      m -> m.j = 1.0
    ).stream().forEach(c -> {
      MetaPojo pojo = new MetaPojo();
      c.accept(pojo);

      Assert.assertNotEquals(pojo, new MetaPojo());
      Assert.assertNotEquals(pojo.toString(), new MetaPojo().toString());
    });
  }

  @Test
  public void testSize() {
    Assert.assertEquals(10, META.size());
  }

  @Test
  public void testNames() {
    // setup
    Set<String> expected = new HashSet<>(asList("a", "b", "c", "d", "e", "f", "g", "h", "i", "j"));
    // invoke
    Set<String> actual = new HashSet<>();
    for (int i = 0; i < META.size(); i++) {
      String name = META.nameAt(i);
      actual.add(name);
    }
    // verify
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testPojo() {
    //MetaPojo pojo = new MetaPojo();
  }

  @Test
  public void testSubclass() {
    MetaPojo base = new MetaPojo();
    MetaPojo sub = new MetaPojo() {};

    Assert.assertEquals(base, sub);
    Assert.assertEquals(sub, base);
  }

  private static final Meta<MetaPojo> META = Meta.meta(MetaPojo.class)
    .namedBoolean("a", o -> o.a)
    .namedChar("b", o -> o.b)
    .namedByte("c", o -> o.c)
    .namedShort("d", $ -> $.d)
    .namedInt("e", $ -> $.e)
    .namedLong("f", $ -> $.f)
    .namedFloat("g", $ -> $.g)
    .namedObject("h", $ -> $.h)
    .namedObject("i", $ -> $.i)
    .namedDouble("j", $ -> $.j)
    .instanceCheckPolicy(Meta.InstanceCheckPolicy.instanceOf());

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
