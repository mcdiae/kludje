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

package uk.kludje.testcontract.test;

import org.junit.Test;
import uk.kludje.testcontract.iterator.HasNextContract;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class HasNextContractTest {

  @Test
  public void testDrainAndAssertElementsNotNull() {
    List<String> str = Collections.singletonList("foo");
    HasNextContract.drainAndAssertElementsNotNull(str.iterator());
  }


}
