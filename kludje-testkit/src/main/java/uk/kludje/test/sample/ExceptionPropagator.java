package uk.kludje.test.sample;

import uk.kludje.Exceptions;

import java.io.IOException;

public class ExceptionPropagator {
  public static void throwIOException() {
    Exceptions.throwChecked(new IOException());
  }

  public static void main(String[] args) {
    try {
      throwIOException();

      Exceptions.<IOException>expected();
    } catch(IOException e) {
      System.out.println("Got it");
    }
  }
}
