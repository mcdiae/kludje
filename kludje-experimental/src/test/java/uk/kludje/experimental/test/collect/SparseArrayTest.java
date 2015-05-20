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

package uk.kludje.experimental.test.collect;

import org.junit.Assert;
import org.junit.Test;
import uk.kludje.experimental.collect.IntSet;
import uk.kludje.experimental.collect.SparseArray;
import uk.kludje.experimental.collect.SparseArrays;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by user on 20/05/15.
 */
public class SparseArrayTest {

  @Test
  public void testLinearSparseArrayMutation() {
    SparseArray<String> sa = SparseArrays.linearSparseArray(1);
    testMutation(sa);
    testEquality(sa, sa.asUnmodifiable());
  }

  @Test
  public void testLinearSparseArrayMutationLargeIncrement() {
    SparseArray<String> sa = SparseArrays.linearSparseArray(100);
    testMutation(sa);
    testEquality(sa, sa.asUnmodifiable());
  }

  private void testMutation(SparseArray<String> sparseArray) {
    Assert.assertEquals(0, sparseArray.size());
    // test put
    sparseArray.put(10, "10");
    sparseArray.put(-2, "-2");
    sparseArray.put(11, "11");
    Assert.assertEquals(3, sparseArray.size());
    Assert.assertEquals("10", sparseArray.get(10));
    Assert.assertEquals("-2", sparseArray.get(-2));
    Assert.assertEquals("11", sparseArray.get(11));
    // test replace
    String old = sparseArray.put(10, "101010");
    Assert.assertEquals(3, sparseArray.size());
    Assert.assertEquals("10", old);
    Assert.assertEquals("101010", sparseArray.get(10));
    Assert.assertEquals("-2", sparseArray.get(-2));
    Assert.assertEquals("11", sparseArray.get(11));
    // test remove
    String removed = sparseArray.remove(10);
    Assert.assertEquals("101010", removed);
    Assert.assertNull(sparseArray.get(10));
    Assert.assertEquals(2, sparseArray.size());
    Assert.assertEquals("-2", sparseArray.get(-2));
    Assert.assertEquals("11", sparseArray.get(11));
  }

  private void testEquality(SparseArray<?> sa1, SparseArray<?> sa2) {
    // test equality
    Assert.assertEquals(sa1, sa2);
    Assert.assertEquals(sa1.hashCode(), sa2.hashCode());
    Assert.assertEquals(sa1.toString(), sa2.toString());
    // check keys
    IntSet k1 = sa1.keys();
    IntSet k2 = sa2.keys();
    Assert.assertEquals(k1, k2);
    Assert.assertEquals(k1.hashCode(), k2.hashCode());
    // check values
    Collection<?> c1 = sa1.values();
    Collection<?> c2 = sa2.values();
    Assert.assertEquals(c1.size(), c2.size());
    Set<?> s1 = new HashSet<>(c1);
    Set<?> s2 = new HashSet<>(c2);
    Assert.assertEquals(s1, s2);
  }
}
