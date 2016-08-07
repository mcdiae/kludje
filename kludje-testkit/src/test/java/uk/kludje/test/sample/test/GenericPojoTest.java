package uk.kludje.test.sample.test;

import org.junit.Test;
import uk.kludje.test.GenericPojo;

import static org.junit.Assert.assertEquals;

public class GenericPojoTest {

  @Test
  public void testGenericPojo() {
    Object o = "foo";
    assertEquals(new GenericPojo<Object>(o), new GenericPojo<Object>(o));
    assertEquals(new GenericPojo<Object>(o).hashCode(), new GenericPojo<Object>(o).hashCode());
    assertEquals(new GenericPojo<Object>(o).toString(), new GenericPojo<Object>(o).toString());
  }
}
