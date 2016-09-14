package uk.kludje.experimental.property.reflect;

import java.io.DataOutput;
import java.io.IOException;

final class ClassConstantAny extends ClassConstant {

  private final byte[] data;

  ClassConstantAny(ClassConstantType type, byte[] data) {
    super(type);
    this.data = data;
  }

  @Override
  void writeInternal(DataOutput output) throws IOException {
    output.write(data);
  }

  @Override
  public String toString() {
    return type + " bytes=" + data.length;
  }
}
