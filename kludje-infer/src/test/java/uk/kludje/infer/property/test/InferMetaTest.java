package uk.kludje.infer.property.test;

import org.junit.Assert;
import org.junit.Test;
import uk.kludje.Meta;
import uk.kludje.infer.property.InferMeta;

public class InferMetaTest {

  private static final Meta<Pojo> META = InferMeta.from(Meta.meta(Pojo.class))
    .objects(Pojo::getA)
    .meta();

  @Test
  public void testEquality() {
    // setup
    Pojo p = new Pojo();
    p.a = "Hello";
    // invoke
    String pojoDebugString = p.toString();
    // verify
    Assert.assertEquals("Pojo {a=Hello}", pojoDebugString);
  }

  private static class Pojo {
    Object a;

    Object getA() {
      return a;
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
