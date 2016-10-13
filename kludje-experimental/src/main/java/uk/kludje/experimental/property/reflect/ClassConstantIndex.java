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

final class ClassConstantIndex extends ClassConstant {

  public final int index;

  ClassConstantIndex(ClassConstantType type, int index) {
    super(type);
    this.index = index;
  }

  @Override
  void writeInternal(DataOutput output) throws IOException {
    output.writeShort(index);
  }

  @Override
  public String toString() {
    return type + " data=" + index;
  }
}
