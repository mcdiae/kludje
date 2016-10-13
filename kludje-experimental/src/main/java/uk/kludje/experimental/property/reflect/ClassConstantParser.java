/*
Copyright 2016 McDowell

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

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
