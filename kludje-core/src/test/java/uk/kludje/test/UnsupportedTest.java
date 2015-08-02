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

package uk.kludje.test;

import org.junit.Test;
import uk.kludje.Unsupported;
import uk.kludje.fn.nary.TriConsumer;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class UnsupportedTest {

  @Test(expected = UnsupportedOperationException.class)
  public void testRun() {
    Runnable runnable = Unsupported::exception;
    runnable.run();
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testConsume1() {
    Consumer<String> fun = Unsupported::exception;
    fun.accept(null);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testConsume2() {
    BiConsumer<String, String> fun = Unsupported::exception;
    fun.accept(null, null);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testConsume3() {
    TriConsumer<String, String, String> fun = Unsupported::exception;
    fun.accept(null, null, null);
  }
}
