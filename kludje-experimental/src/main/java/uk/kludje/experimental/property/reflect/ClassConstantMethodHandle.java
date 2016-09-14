package uk.kludje.experimental.property.reflect;

import java.io.DataOutput;
import java.io.IOException;

final class ClassConstantMethodHandle extends ClassConstant {
  public final int kind;
  public final int index;

  ClassConstantMethodHandle(int kind, int index) {
    super(ClassConstantType.METHODHANDLE);
    this.kind = kind;
    this.index = index;
  }

  @Override
  void writeInternal(DataOutput output) throws IOException {
    output.writeByte(kind);
    output.writeShort(index);
  }

  @Override
  public String toString() {
    return type + " kind=" + kind + " index=" + index;
  }
}
