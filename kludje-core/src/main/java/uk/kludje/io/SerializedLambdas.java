package uk.kludje.io;

import uk.kludje.Ensure;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;

public final class SerializedLambdas {

  private SerializedLambdas() {
  }

  /**
   * This method will throw a {@link NotASerializedLambdaException} if the argument is not a lambda.
   * This method will throw a {@link InspectionException} if the argument cannot be inspected by reflection for any reason.
   *
   * @param lambda must be a lambda or method reference that implements a FunctionalInterface that extends Serializable
   * @return the SerializedLambda form of the lambda
   */
  public static SerializedLambda toSerializedLambda(Serializable lambda) {
    Ensure.that(lambda != null, "lambda != null");

    try {
      Method writeReplace = lambda.getClass().getDeclaredMethod("writeReplace");
      writeReplace.setAccessible(true);
      Object replacement = writeReplace.invoke(lambda);
      if (!(replacement instanceof SerializedLambda)) {
        throw new NotASerializedLambdaException("Not an instance of SerializedLambda: " + replacement);
      }
      return (SerializedLambda) replacement;
    } catch (NoSuchMethodException e) {
      throw new NotASerializedLambdaException("Not a lambda: " + lambda);
    } catch (Exception e) {
      throw new InspectionException(e);
    }
  }

  public static class SerializedLambdaException extends RuntimeException {

    SerializedLambdaException(Exception e) {
      super(e);
    }

    SerializedLambdaException(String message) {
      super(message);
    }
  }

  public static class InspectionException extends SerializedLambdaException {
    InspectionException(Exception e) {
      super(e);
    }
  }

  public static class NotASerializedLambdaException extends SerializedLambdaException {
    NotASerializedLambdaException(String message) {
      super(message);
    }
  }
}
