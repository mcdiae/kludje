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
import uk.kludje.experimental.collect.CollectionAdapter;

import java.util.*;

public class CollectionAdapterTest {

  @Test
  public void testAsList() {
    // setup
    List<String> expected = Arrays.asList("foo", "bar");
    // invoke
    List<String> actual = CollectionAdapter.asList(expected::get, expected::size);
    // verify
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testAsSet() {
    // setup
    Set<String> expected = new HashSet<>(Arrays.asList("foo", "bar"));
    // invoke
    Set<String> actual = CollectionAdapter.asSet(expected::iterator, expected::size);
    // verify
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testAsMap() {
    // setup
    Map<String, String> expected = new HashMap<>();
    expected.put("foo", "bar");
    expected.put("bar", "baz");
    // invoke
    Map<String, String> actual = CollectionAdapter.asMap(expected.entrySet());
    // verify
    Assert.assertEquals(expected, actual);
  }
}
