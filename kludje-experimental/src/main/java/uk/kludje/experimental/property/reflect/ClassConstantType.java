package uk.kludje.experimental.property.reflect;

enum ClassConstantType {
  CLASS(7),
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
  METHODHANLDE(15),
  METHODTYPE(16),
  INVOKEDYNAMIC(18);

  public final byte val;

  ClassConstantType(int val) {
    this.val = (byte) val;
  }
}
