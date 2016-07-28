package uk.kludje.io.test;

import org.junit.Assert;
import org.junit.Test;
import uk.kludje.io.SerializedLambdas;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;

public class SerializedLambdasTest {

  @Test
  public void testToSerializedLambda() {
    // init
    Foo foo = this::testToSerializedLambda;
    // invoke
    SerializedLambda serializedLambda = SerializedLambdas.toSerializedLambda(foo);
    // verify
    Assert.assertNotNull(serializedLambda);
  }

  @Test(expected = SerializedLambdas.NotASerializedLambdaException.class)
  public void testNotASerializedLambda() {
    // init
    Foo foo = new Foo() {
      @Override
      public void foo() {}
    };
    // invoke
    SerializedLambda serializedLambda = SerializedLambdas.toSerializedLambda(foo);
    // verify
    Assert.assertNotNull(serializedLambda);
  }

  @FunctionalInterface
  private interface Foo extends Serializable {
    void foo();
  }
}
