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

package uk.kludje.test.array;

import org.junit.Assert;
import org.junit.Test;
import uk.kludje.collect.MutableSparseArray;
import uk.kludje.collect.array.ArrayCollections;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MutableSparseArrayTest {

  @Test
  public void testPutAndGet() {
    String foo = "foo";
    String bar = "bar";

    MutableSparseArray<String> sparseArray = ArrayCollections.mutableSparseArray();
    Assert.assertNotNull(sparseArray);
    Assert.assertEquals(0, sparseArray.size());
    Assert.assertEquals(0, sparseArray.keyStream().count());
    Assert.assertNull(sparseArray.get(Integer.MIN_VALUE));

    sparseArray.put(Integer.MIN_VALUE, foo);
    Assert.assertEquals(1, sparseArray.size());
    Assert.assertEquals(1, sparseArray.keyStream().count());

    sparseArray.put(Integer.MAX_VALUE, foo);
    Assert.assertEquals(2, sparseArray.size());
    Assert.assertEquals(2, sparseArray.keyStream().count());

    sparseArray.put(Integer.MAX_VALUE, bar);
    Assert.assertEquals(2, sparseArray.size());
    Assert.assertEquals(2, sparseArray.keyStream().count());

    Assert.assertEquals(foo, sparseArray.get(Integer.MIN_VALUE));
    Assert.assertEquals(bar, sparseArray.get(Integer.MAX_VALUE));
  }

  @Test
  public void testGrow() {
    String foo = "foo";
    int limit = 10_000;

    MutableSparseArray<String> sparseArray = ArrayCollections.mutableSparseArray();
    for (int i = 0; i < limit; i++) {
      sparseArray.put(i, foo);
    }
    Assert.assertEquals(limit, sparseArray.size());
  }

  @Test
  public void testRemoveHead() {
    int limit = 10;

    MutableSparseArray<Integer> sparseArray = ArrayCollections.mutableSparseArray();
    Assert.assertNotNull(sparseArray);

    for (int i = 0; i < limit; i++) {
      sparseArray.put(i, i);
    }

    Assert.assertEquals(limit, sparseArray.size());

    for (int i = 0; i < limit; i++) {
      boolean changed = sparseArray.remove(i);
      Assert.assertTrue(changed);
      Assert.assertNull(sparseArray.get(i));
    }

    Assert.assertEquals(0, sparseArray.size());
  }

  @Test
  public void testRemoveTail() {
    int limit = 10;

    MutableSparseArray<Integer> sparseArray = ArrayCollections.mutableSparseArray();
    Assert.assertNotNull(sparseArray);

    for (int i = 0; i < limit; i++) {
      sparseArray.put(i, i);
    }

    Assert.assertEquals(limit, sparseArray.size());

    for (int i = 0; i < limit; i++) {
      boolean changed = sparseArray.remove(i);
      Assert.assertTrue(changed);
      Assert.assertNull(sparseArray.get(i));
    }

    Assert.assertEquals(0, sparseArray.size());
  }

  @Test
  public void testRemoveBody() {
    int limit = 10;

    MutableSparseArray<Integer> sparseArray = ArrayCollections.mutableSparseArray();
    Assert.assertNotNull(sparseArray);

    List<Integer> keys = new LinkedList<>();
    for (int i = 0; i < limit; i++) {
      if (i % 2 == 0) {
        keys.add(i);
      } else {
        keys.add(0, i);
      }

      sparseArray.put(i, i);
    }

    Assert.assertEquals(limit, sparseArray.size());
    Assert.assertFalse(sparseArray.isEmpty());
    Assert.assertTrue(sparseArray.isNotEmpty());

    for (int key : keys) {
      boolean changed = sparseArray.remove(key);
      Assert.assertTrue(changed);
      Assert.assertNull(sparseArray.get(key));
    }

    Assert.assertEquals(0, sparseArray.size());
    Assert.assertTrue(sparseArray.isEmpty());
    Assert.assertFalse(sparseArray.isNotEmpty());
  }

  @Test
  public void testKeyStream() {
    int limit = 10;

    MutableSparseArray<Integer> sparseArray = ArrayCollections.mutableSparseArray();
    Assert.assertNotNull(sparseArray);

    Set<Integer> expectedKeys = new HashSet<>();
    int removed = limit / 2;
    for (int i = 0; i < limit; i++) {
      sparseArray.put(i, i);
      if (i != removed) {
        expectedKeys.add(i);
      }
    }
    sparseArray.remove(removed);
    Assert.assertEquals(expectedKeys.size(), sparseArray.size());
    // begin test
    IntStream stream = sparseArray.keyStream();
    Assert.assertNotNull(stream);
    Set<Integer> actualKeys = stream.mapToObj(i -> i).collect(Collectors.toSet());
    Assert.assertEquals(expectedKeys, actualKeys);
  }

  @Test
  public void testContains() {
    int limit = 10;

    MutableSparseArray<Integer> sparseArray = ArrayCollections.mutableSparseArray();
    Assert.assertNotNull(sparseArray);

    Set<Integer> expectedKeys = new HashSet<>();
    int removed = limit / 2;
    for (int i = 0; i < limit; i++) {
      sparseArray.put(i, i);
      if (i != removed) {
        expectedKeys.add(i);
      }
    }
    sparseArray.remove(removed);
    Assert.assertEquals(expectedKeys.size(), sparseArray.size());
    // begin test
    for(Integer key: expectedKeys) {
      Assert.assertTrue(key.toString(), sparseArray.contains(key));
    }
  }
}
