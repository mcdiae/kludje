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

package uk.kludje.testcontract.iterator;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static uk.kludje.testcontract.ContractViolationException.assertThat;

/**
 * Tests for verifying the implementation of {@link Iterator}.
 */
public final class IteratorContract {

  private IteratorContract() {}

  public static final <T> void drainAndAssertElementsNotNull(Iterator<T> iterator) {

    while (iterator.hasNext()) {
      T t = iterator.next();
      assertThat(t != null, "t != null");
    }

    assertEmptyIteratorThrowsNoSuchElement(iterator);
  }

  public static final <T> void assertEmptyIteratorThrowsNoSuchElement(Iterator<T> iterator) {
    assertThat(!iterator.hasNext(), "!iterator.hasNext()");

    try {
      iterator.next();

      assertThat(false, "Expected " + NoSuchElementException.class.getName());
    } catch (NoSuchElementException e) {
      // expected
    }
  }
}
