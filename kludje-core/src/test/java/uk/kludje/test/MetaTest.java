package uk.kludje.test;

import org.junit.Assert;
import org.junit.Test;
import uk.kludje.Meta;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class MetaTest {
  @Test
  public void basicTest() {
    Assert.assertEquals(new MetaPojo(), new MetaPojo());
    Assert.assertEquals(new MetaPojo().hashCode(), new MetaPojo().hashCode());
    Assert.assertEquals(new MetaPojo().toString(), new MetaPojo().toString());
  }

  @Test
  public void comboTest() {
    Arrays.<Consumer<MetaPojo>>asList(
        m -> m.a = true,
        m -> m.b = 'a',
        m -> m.c = -1,
        m -> m.d = -2,
        m -> m.e = 10,
        m -> m.f = 5l,
        m -> m.g = 1.0f,
        m -> m.h = new Object(),
        m -> m.i = "",
        m -> m.j = 1.0
    ).stream().forEach(c -> {
      MetaPojo pojo = new MetaPojo();
      c.accept(pojo);
      Assert.assertFalse(pojo.equals(new MetaPojo()));
      Assert.assertFalse(pojo.toString().equals(new MetaPojo().toString()));
    });
  }

  private static final Meta<MetaPojo> META = Meta.meta(MetaPojo.class)
      .booleans($ -> $.a)
      .chars($ -> $.b)
      .bytes($ -> $.c)
      .shorts($ -> $.d)
      .ints($ -> $.e)
      .longs($ -> $.f)
      .floats($ -> $.g)
      .objects($ -> $.h, $ -> $.i)
      .doubles($ -> $.j);

  class MetaPojo {
    boolean a;
    char b;
    byte c;
    short d;
    int e;
    long f;
    float g;
    Object h;
    String i;
    double j;

    @Override
    public boolean equals(Object obj) {
      return META.equals(this, obj);
    }

    @Override
    public int hashCode() {
      return META.hashCode(this);
    }

    @Override
    public String toString() {
      return META.toString(this);
    }
  }
}
