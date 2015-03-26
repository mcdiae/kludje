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
import uk.kludje.experimental.collect.AdapterCollections;

import java.util.*;

public class AdapterCollectionsTest {

  @Test
  public void testAsCollection() {
    // setup
    Collection<String> expected = Arrays.asList("foo", "bar");
    // invoke
    Collection<String> actual = AdapterCollections.<String>collection(expected::iterator, expected::size);
    // verify
    Assert.assertEquals(expected.size(), actual.size());
    Assert.assertTrue(actual.contains("foo"));
    Assert.assertTrue(actual.contains("bar"));
    Assert.assertFalse(actual.contains("baz"));
  }

  @Test
  public void testAsList() {
    // setup
    List<String> expected = Arrays.asList("foo", "bar");
    // invoke
    List<String> actual = AdapterCollections.list(expected::get, expected::size);
    // verify
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testAsSet() {
    // setup
    Set<String> expected = new HashSet<>(Arrays.asList("foo", "bar"));
    // invoke
    Set<String> actual = AdapterCollections.set(expected::iterator, expected::size);
    // verify
    Assert.assertEquals(expected, actual);
  }
}
