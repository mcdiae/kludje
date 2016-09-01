package uk.kludje.experimental.test.infer.property;

import org.junit.Assert;
import org.junit.Test;
import uk.kludje.Meta;
import uk.kludje.experimental.infer.property.NamedGetterList;

public class NamedGetterListTest {

  @Test
  public void testEquality() {
    // setup
    Pojo p = new Pojo();
    p.stringProp = "Hello";
    // invoke
    String pojoDebugString = p.toString();
    // verify
    Assert.assertEquals("Pojo {stringProp=Hello}", pojoDebugString);
  }

  private static class Pojo {
    private static final Meta<Pojo> META = NamedGetterList.namer(Meta.meta(Pojo.class))
      .objects(Pojo::getStringProp)
      .list();

    Object stringProp;

    Object getStringProp() {
      return stringProp;
    }

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
