package uk.kludje.experimental.property.reflect;

import java.io.DataOutput;
import java.io.IOException;

final class ClassConstantUtf8 extends ClassConstant {

  public final String data;

  ClassConstantUtf8(String data) {
    super(ClassConstantType.UTF8);
    this.data = data;
  }

  @Override
  void writeInternal(DataOutput output) throws IOException {
    output.writeUTF(data);
  }

  @Override
  public String toString() {
    return type + " " + data;
  }
}
