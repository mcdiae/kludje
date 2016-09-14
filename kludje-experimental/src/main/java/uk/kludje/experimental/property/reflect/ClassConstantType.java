package uk.kludje.experimental.property.reflect;

import java.io.DataOutput;
import java.io.IOException;

enum ClassConstantType implements DataWriter {
  CLASS(7) ,
  FIELDREF(9),
  METHODREF(10),
  INTERFACEMETHODREF(11),
  STRING(8),
  INTEGER(3),
  FLOAT(4),
  LONG(5),
  DOUBLE(6),
  NAMEANDTYPE(12),
  UTF8(1),
  METHODHANDLE(15),
  METHODTYPE(16),
  INVOKEDYNAMIC(18);

  public final int tag;

  ClassConstantType(int val) {
    this.tag = val;
  }

  @Override
  public void writeTo(DataOutput output) throws IOException {
    output.writeByte(tag);
  }

  public static ClassConstantType fromTag(int tag) {
    for (ClassConstantType type : values()) {
      if (type.tag == tag) {
        return type;
      }
    }
    throw new IllegalArgumentException("Unknown constant tag: " + tag);
  }
}
