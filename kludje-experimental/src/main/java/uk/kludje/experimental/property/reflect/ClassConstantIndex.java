package uk.kludje.experimental.property.reflect;

import java.io.DataOutput;
import java.io.IOException;

final class ClassConstantIndex extends ClassConstant {

  public final int index;

  ClassConstantIndex(ClassConstantType type, int index) {
    super(type);
    this.index = index;
  }

  @Override
  void writeInternal(DataOutput output) throws IOException {
    output.writeShort(index);
  }

  @Override
  public String toString() {
    return type + " data=" + index;
  }
}
