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

import org.junit.Assert;
import org.junit.Test;
import uk.kludje.CloseableResource;
import uk.kludje.Exceptions;

import java.util.concurrent.atomic.AtomicBoolean;

public class CloseableResourceTest {

  @Test
  public void compileTest() {
    try(CloseableResource res = this::noop) {
    }
  }

  @Test
  public void exceptionTest() {
    AtomicBoolean gotit = new AtomicBoolean();
    try(CloseableResource res = throwCre().<CRE>expected()) {
    } catch (CRE e) {
      gotit.set(true);
    }
    Assert.assertTrue(gotit.get());
  }

  private CloseableResource throwCre() {
    return () -> Exceptions.throwChecked(new CRE());
  }

  private void noop() {}

  private static class CRE extends Exception {
  }
}
