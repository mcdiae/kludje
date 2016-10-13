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
