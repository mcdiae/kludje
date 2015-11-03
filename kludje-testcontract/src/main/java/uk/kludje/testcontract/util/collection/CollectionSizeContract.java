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

package uk.kludje.testcontract.util.collection;

import uk.kludje.testcontract.ContractViolationException;

import java.util.Collection;
import java.util.Iterator;

public class CollectionSizeContract {

  public static <E> void assertIterator(Collection<E> coll) {
    Iterator<E> it = coll.iterator();
    int size = 0;
    while (it.hasNext()) {
      it.next();
      size++;
    }
    ContractViolationException.assertThat(size == coll.size(), "assertIteratorSize: " + coll.getClass());
  }

  public static <E> void assertStreamSize(Collection<E> coll) {
    ContractViolationException.assertThat(coll.stream().count() == coll.size(), "assertStreamSize: " + coll.getClass());
  }

  public static <E> void assertToArraySize(Collection<E> coll) {
    ContractViolationException.assertThat(coll.toArray().length == coll.size(), "assertToArraySize: " + coll.getClass());
  }


}
