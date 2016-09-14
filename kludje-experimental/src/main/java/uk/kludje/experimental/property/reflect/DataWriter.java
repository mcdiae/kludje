package uk.kludje.experimental.property.reflect;

import java.io.DataOutput;
import java.io.IOException;

interface DataWriter {

  void writeTo(DataOutput output) throws IOException;
}
