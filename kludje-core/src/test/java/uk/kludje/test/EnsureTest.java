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
import uk.kludje.Ensure;

@Deprecated
public class EnsureTest {

  @Test
  public void testOk() throws Exception{
    Ensure.that(true, "unexpected");
    Ensure.checked(true, "unexpected", Exception::new);
    Ensure.unchecked(true, "unexpected", Exception::new);
  }

  @Test(expected = AssertionError.class)
  public void testThat() {
    Ensure.that(false, "unexpected");
  }

  @Test(expected = Exception.class)
  public void testChecked() throws Exception {
    Ensure.checked(false, "expected", Exception::new);
  }

  @Test(expected = Exception.class)
  public void testUnchecked() {
    Ensure.unchecked(false, "expected", Exception::new);
  }
}
