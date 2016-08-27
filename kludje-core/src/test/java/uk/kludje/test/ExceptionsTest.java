/*
Copyright 2014 McDowell

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package uk.kludje.test;

import org.junit.Test;
import uk.kludje.Exceptions;

import java.io.IOException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ExceptionsTest {

  @Test(expected = IOException.class)
  public void testThrowChecked() {
    throw Exceptions.throwChecked(new IOException("expected"));
  }

  @Test
  public void testCatch() {
    boolean gotIt = false;
    try {
      Exceptions
        .<IOException>expected()
        .<ClassNotFoundException>expected();

      throw Exceptions.throwChecked(new IOException("expected"));
    } catch (IOException | ClassNotFoundException e) {
      gotIt = true;
    }
    assertTrue(gotIt);
  }

  @Test
  public void testCatch2() {
    boolean gotIt = false;
    try {
      Exceptions
        .<IOException>expected()
        .<ClassNotFoundException>expected();

      throw Exceptions.throwChecked(new ClassNotFoundException("expected"));
    } catch (IOException | ClassNotFoundException e) {
      gotIt = true;
    }
    assertTrue(gotIt);
  }

  @Test
  public void testExpectedDoesNothing() {
    boolean gotIt = false;
    try {
      Exceptions
        .<IOException>expected()
        .<ClassNotFoundException>expected();
    } catch (IOException | ClassNotFoundException e) {
      gotIt = true;
    }
    assertFalse(gotIt);
  }

  @Test
  public void testLoadMarker() {
    // really just for coverage's sake
    assertTrue(Error.class.isAssignableFrom(Exceptions.UncheckedMarker.class));
  }
}
