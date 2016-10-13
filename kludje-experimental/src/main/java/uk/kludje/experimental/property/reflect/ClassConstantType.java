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
