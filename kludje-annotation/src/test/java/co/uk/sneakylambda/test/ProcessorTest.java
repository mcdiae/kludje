package co.uk.sneakylambda.test;

import co.uk.kludje.annotation.processor.UncheckedFunctionalInterfaceProcessor;
import org.junit.Test;

public class ProcessorTest {

  @Test
  public void loadType() {
    new UncheckedFunctionalInterfaceProcessor();
  }
}
