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
import uk.kludje.collect.array.ArrayCollections;
import uk.kludje.testcontract.lang.object.EqualsContract;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class MutableSortedSetTest {

  @Test
  public void testObjectContract() {
    String foo = "foo";

    Set<String> hash = new HashSet<>();
    Set<String> mutable = ArrayCollections.mutableSortedSet();
    Set<String> mutable2 = ArrayCollections.mutableSortedSet();

    Assert.assertEquals(hash, mutable);
    Assert.assertEquals(mutable, mutable2);
    Assert.assertEquals(hash.toString(), mutable.toString());

    EqualsContract.assertAll(mutable, hash, mutable2);

    mutable.add(foo);
    hash.add(foo);

    Assert.assertEquals(hash, mutable);
    Assert.assertEquals(hash.size(), mutable.size());
    Assert.assertNotEquals(mutable, mutable2);
    Assert.assertEquals(hash.toString(), mutable.toString());

    EqualsContract.assertAll(mutable, hash, mutable2);
  }

  //@Test
  public void testAddRemove() {

    Set<Integer> mutable = ArrayCollections.mutableSortedSet();

    mutable.add(2);
    mutable.add(1);
    mutable.add(4);
    mutable.add(3);
    mutable.add(5);

    Assert.assertEquals(5, mutable.size());

    mutable.remove(3);
    mutable.remove(1);
    mutable.remove(5);

    Assert.assertEquals(2, mutable.size());

    Assert.assertFalse(mutable.contains(1));
    Assert.assertTrue(mutable.contains(2));
    Assert.assertFalse(mutable.contains(3));
    Assert.assertTrue(mutable.contains(4));
    Assert.assertFalse(mutable.contains(5));
  }
}
