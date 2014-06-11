package uk.kludje.annotation.processor.test;

import uk.kludje.annotation.processor.UncheckedFunctionalInterfaceProcessor;
import org.junit.Test;

public class ProcessorTest {

  @Test
  public void loadType() {
    new UncheckedFunctionalInterfaceProcessor();
  }
}
