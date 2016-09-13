package uk.kludje.experimental.property.reflect;

import java.io.DataOutput;
import java.io.IOException;

final class ClassConstantIndex2 extends ClassConstant {

  public final int index1;
  public final int index2;

  ClassConstantIndex2(ClassConstantType type, int index1, int index2) {
    super(type);
    this.index1 = index1;
    this.index2 = index2;
  }

  @Override
  void writeInternal(DataOutput output) throws IOException {
    output.writeShort(index1);
    output.writeShort(index2);
  }

  @Override
  public String toString() {
    return type + " index1=" + index1 + " index2=" + index2;
  }
}
