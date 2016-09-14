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

import java.util.Arrays;
import java.util.function.Consumer;

/**
 * Will be removed; checks deprecated meta() method
 */
@Deprecated
public class DeprecatedMetaTest {

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
        m -> m.f = 5L,
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

  private static final Meta<MetaPojo> META = Meta.<MetaPojo>meta()
      .booleans($ -> $.a)
      .chars($ -> $.b)
      .bytes($ -> $.c)
      .shorts($ -> $.d)
      .ints($ -> $.e)
      .longs($ -> $.f)
      .floats($ -> $.g)
      .objects($ -> $.h, $ -> $.i)
      .doubles($ -> $.j);

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
