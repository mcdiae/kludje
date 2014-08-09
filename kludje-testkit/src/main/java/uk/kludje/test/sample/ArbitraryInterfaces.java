/*
 *
 * Copyright $year McDowell
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
 * /
 */

package uk.kludje.test.sample;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import static uk.kludje.fn.nary.UTriFunction.asUTriFunction;

public class ArbitraryInterfaces {

  public void invoke() throws IOException {
    call(asUTriFunction(this::download)::apply);
  }

  /**This method doesn't work but it might throw an exception*/
  @SuppressWarnings("try")
  private boolean download(File target, URI source, Object ignored) throws IOException {
    try (InputStream in = source.toURL().openStream()) {
      return true;
    }
  }

  private void call(Foo foo) {
    foo.exec(new File("foo.txt"), URI.create("http://foo"), null);
  }

  public static interface Foo {
    boolean exec(File a, URI b, Object x);
  }
}
