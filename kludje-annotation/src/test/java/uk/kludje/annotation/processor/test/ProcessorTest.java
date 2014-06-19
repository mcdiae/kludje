package uk.kludje.annotation.processor.test;

import org.junit.Test;
import uk.kludje.annotation.processor.UncheckedFunctionalInterfaceProcessor;

public class ProcessorTest {

  @Test
  public void loadType() {
    new UncheckedFunctionalInterfaceProcessor();
  }
}
