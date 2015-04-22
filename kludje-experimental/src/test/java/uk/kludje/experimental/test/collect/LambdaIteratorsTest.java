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
import static uk.kludje.experimental.collect.LambdaIterators.*;

import java.util.ArrayList;
import static java.util.Arrays.asList;
import java.util.Iterator;
import java.util.List;

public class LambdaIteratorsTest {

  @Test
  public void testIterator() {
    // setup
    List<String> expected = asList("a", "b", "c");
    Iterator<String> src = expected.iterator();
    // invoke
    List<String> actual = toList(iterator(src::hasNext, src::next));
    // verify
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testMutableIterator() {
    // setup
    List<String> expected = asList("a", "b", "c");
    Iterator<String> src = expected.iterator();
    // invoke
    List<String> actual = toList(mutableIterator(src::hasNext, src::next, src::remove));
    // verify
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testMutableIteratorRemove() {
    // setup
    List<String> list = new ArrayList<>(asList("a", "b", "c"));
    Iterator<String> src = list.iterator();
    // invoke
    removeAll(mutableIterator(src::hasNext, src::next, src::remove));
    // verify
    Assert.assertTrue(list.isEmpty());
  }

  @Test
  public void testIndexIterator() {
    // setup
    String[] expected = {"a", "b", "c"};
    // invoke
    Iterator<String> it = indexIterator(i -> i < expected.length, i -> expected[i]);
    List<String> actual = toList(it);
    // verify
    Assert.assertEquals(asList(expected), actual);
  }

  @Test
  public void testMutableIndexIterator() {
    // setup
    String[] expected = {"a", "b", "c"};
    // invoke
    Iterator<String> it = mutableIndexIterator(i -> (i < expected.length), i -> expected[i], i -> {
    });
    List<String> actual = toList(it);
    // verify
    Assert.assertEquals(asList(expected), actual);
  }

  @Test
  public void testMutableIndexIteratorRemove() {
    // setup
    String[] expected = {"a", "b", "c"};
    List<Integer> removed = new ArrayList<>();
    // invoke
    Iterator<String> it = mutableIndexIterator(i -> (i < expected.length), i -> expected[i], removed::add);
    removeAll(it);
    // verify
    Assert.assertEquals(asList(0, 1, 2), removed);
  }

  @Test
  public void testArrayIterator() {
    // setup
    String[] expected = {"a", "b", "c"};
    // invoke
    Iterator<String> it = arrayIterator(expected);
    List<String> actual = toList(it);
    // verify
    Assert.assertEquals(asList(expected), actual);
  }

  @Test
  public void testMutableArrayIterator() {
    // setup
    String[] expected = {"a", "b", "c"};
    // invoke
    Iterator<String> it = mutableArrayIterator(i -> {
    }, expected);
    List<String> actual = toList(it);
    // verify
    Assert.assertEquals(asList(expected), actual);
  }

  @Test
  public void testMutableArrayIteratorRemove() {
    // setup
    String[] expected = {"a", "b", "c"};
    List<Integer> removed = new ArrayList<>();
    // invoke
    Iterator<String> it = mutableArrayIterator(removed::add, expected);
    removeAll(it);
    // verify
    Assert.assertEquals(asList(0, 1, 2), removed);
  }

  private <E> List<E> toList(Iterator<E> it) {
    List<E> actual = new ArrayList<>();
    it.forEachRemaining(actual::add);
    return actual;
  }

  private void removeAll(Iterator<?> it) {
    while(it.hasNext()) {
      it.next();
      it.remove();
    }
  }
}
