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

import uk.kludje.Ensure;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public final class ClassFileStructure implements DataWriter {

  private static final Log LOG = Log.logger(ClassFileStructure.class);

  public static final int MAGIC = 0xCAFEBABE;
  public static final short MINOR_VERSION = 0;
  public static final short MAJOR_VERSION = 52;

  private final List<ClassConstant> constants;
  private final byte[] remainder;

  ClassFileStructure(List<ClassConstant> constants, byte[] remainder) {
    this.constants = constants;
    this.remainder = remainder;
  }

  @Override
  public void writeTo(DataOutput output) throws IOException {
    output.writeInt(MAGIC);
    output.writeShort(MINOR_VERSION);
    output.writeShort(MAJOR_VERSION);
    output.writeShort(constants.size() + 1);
    for (ClassConstant constant : constants) {
      constant.writeTo(output);
    }
    output.write(remainder);
  }

  public String className() {
    throw new UnsupportedOperationException();
  }

  public byte[] toBytes() {
    throw new UnsupportedOperationException();
  }

  public static ClassFileStructure parse(DataInputStream input) throws IOException {
    Ensure.that(input != null, "input != null");
    Ensure.checked(input.readInt() == MAGIC, "magic == 0xCAFEBABE", IOException::new);
    Ensure.checked(input.readShort() == MINOR_VERSION, "minor == 0", IOException::new);
    Ensure.checked(input.readShort() == MAJOR_VERSION, "major == 52", IOException::new);

    int constPoolSize = input.readUnsignedShort();
    List<ClassConstant> constants = new ArrayList<>(constPoolSize);
    for (int i = 0; i < constPoolSize - 1; i++) {
      ClassConstant constant = ClassConstantParser.parse(input);
      constants.add(constant);

      LOG.debug("Constant %d: %s", i, constant);
    }

    ByteArrayOutputStream remainder = new ByteArrayOutputStream();
    byte[] buf = new byte[1024];
    int r;
    while ((r = input.read(buf)) > -1) {
      remainder.write(buf, 0, r);
    }

    return new ClassFileStructure(constants, remainder.toByteArray());
  }

  public static ClassFileStructure parse(URL url) throws IOException {
    try (InputStream in = url.openStream();
         DataInputStream data = new DataInputStream(in)) {
      return parse(data);
    }
  }
}
