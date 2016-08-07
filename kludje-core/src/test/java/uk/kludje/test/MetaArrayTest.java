/*
Copyright 2016 McDowell

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
package uk.kludje.test;

import org.junit.Assert;
import org.junit.Test;
import uk.kludje.Meta;
import uk.kludje.MetaConfig;

public class MetaArrayTest {
  
  @Test
  public void testEqual() {
    // setup
    PojoWithArrays p1 = new PojoWithArrays();
    PojoWithArrays p2 = new PojoWithArrays();
    // invoke
    boolean equal = p1.equals(p2);
    int hash1 = p1.hashCode();
    int hash2 = p2.hashCode();
    String str1 = p1.toString();
    String str2 = p2.toString();
    // verify
    Assert.assertTrue(equal);
    Assert.assertEquals(hash1, hash2);
    Assert.assertEquals(str1, str2);
  }

  private static final Meta<PojoWithArrays> META = Meta.meta(PojoWithArrays.class)
    .objects(o -> o.a, o -> o.b, o -> o.c, o -> o.d, o -> o.e, o -> o.f, o -> o.g, o -> o.h, o -> o.i, o -> o.j)
    .configure(MetaConfig.defaultConfig().withShallowArraySupport());

  private static class PojoWithArrays {
    boolean[] a = {true, false};
    char[] b = {'a'};
    byte[] c = {0};
    short[] d = {1};
    int[] e = {1, 2, 3};
    long[] f = {1};
    float[] g = {0.0f};
    Object[] h = {};
    String[] i = {"two"};
    double[] j = {};

    @Override
    public boolean equals(Object o) {
      return META.equals(this, o);
    }

    @Override
    public int hashCode() {
      return META.hashCode(this);
    }

    @Override
    public String toString() {
      return super.toString();
    }
  }

}
