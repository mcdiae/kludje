package uk.kludje.experimental.property.reflect;

import java.io.DataInput;
import java.io.IOException;
import java.util.logging.Logger;

final class ClassConstantParser {

  private static final Log LOG = Log.logger(ClassConstantParser.class);

  private ClassConstantParser() {}

  public static ClassConstant parse(DataInput input) throws IOException {

    int tag = input.readUnsignedByte();
    ClassConstantType type = ClassConstantType.fromTag(tag);

    LOG.debug("Constant type: %s", type);

    switch (type) {
      case STRING:
      case CLASS:
      case METHODTYPE:
        int index = input.readUnsignedShort();
        return new ClassConstantIndex(type, index);
      case UTF8:
        String data = input.readUTF();
        return new ClassConstantUtf8(data);
      case FIELDREF:
      case METHODREF:
      case INTERFACEMETHODREF:
      case NAMEANDTYPE:
      case INVOKEDYNAMIC:
        int index1 = input.readUnsignedShort();
        int index2 = input.readUnsignedShort();
        return new ClassConstantIndex2(type, index1, index2);
      case INTEGER:
      case FLOAT:
        byte[] u4 = new byte[4];
        input.readFully(u4);
        return new ClassConstantAny(type, u4);
      case LONG:
      case DOUBLE:
        byte[] u8 = new byte[4];
        input.readFully(u8);
        return new ClassConstantAny(type, u8);
      case METHODHANDLE:
        int kind = input.readUnsignedByte();
        int mhIndex = input.readUnsignedShort();
        return new ClassConstantMethodHandle(kind, mhIndex);
      default:
        throw new IOException("Unsupported constant type: " + type);
    }
  }
}
