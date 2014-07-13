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
