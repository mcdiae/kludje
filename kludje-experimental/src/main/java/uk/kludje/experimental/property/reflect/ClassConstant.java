package uk.kludje.experimental.property.reflect;

import java.io.DataOutput;
import java.io.IOException;

abstract class ClassConstant implements DataWriter {

  public final ClassConstantType type;

  protected ClassConstant(ClassConstantType type) {
    this.type = type;
  }

  @Override
  public final void writeTo(DataOutput output) throws IOException {
    type.writeTo(output);
    writeInternal(output);
  }

  abstract void writeInternal(DataOutput output) throws IOException;
}
