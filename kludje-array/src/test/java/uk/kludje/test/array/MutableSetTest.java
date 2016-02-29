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

import java.util.HashSet;
import java.util.Set;

public class MutableSetTest {

  @Test
  public void testObjectContract() {
    String foo = "foo";
    String bar = "bar";

    Set<String> hash = new HashSet<>();
    Set<String> mutable = ArrayCollections.mutableSet();
    Set<String> mutable2 = ArrayCollections.mutableSet();

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
}
