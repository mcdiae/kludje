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

import junit.framework.Assert;
import org.junit.Test;
import uk.kludje.experimental.collect.IntSet;
import uk.kludje.experimental.collect.LambdaIntSets;

import java.util.Set;

public class LambdaIntSetsTest {

  @Test
  public void testLinearIntSet() {
    int[] contents = {20, 20, 10, 30};
    IntSet set = LambdaIntSets.intSetOf(contents);
    Assert.assertEquals(3, set.size());
    for(int n : contents) {
      Assert.assertTrue(set.contains(n));
    }
    Assert.assertFalse(set.contains(40));
    testUnboxing(set);
  }

  @Test
  public void testEmptySet() {
    testUnboxing(LambdaIntSets.emptyIntSet());
  }

  private void testUnboxing(IntSet set) {
    Set<Integer> unboxed = set.asUnboxedSet();
    Assert.assertEquals(set.size(), unboxed.size());
    IntSet back = LambdaIntSets.lambdaIntSet(() -> unboxed.stream().mapToInt(i -> i));
    Assert.assertTrue(set.equals(back));
    Assert.assertTrue(back.equals(set));
    Assert.assertEquals(set.hashCode(), back.hashCode());
  }
}
