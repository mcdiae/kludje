/*
 * Copyright 2015 McDowell
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.kludje.testcontract.io;

import uk.kludje.testcontract.ContractViolationException;
import java.io.*;

public final class SerializableContract {

  private SerializableContract() {}

  public static <T> T serializeDeserialize(T t) throws IOException {
    byte[] data = serialize(t);
    return deserialize(data);
  }

  private static <T> byte[] serialize(T t) throws IOException {
    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    try(ObjectOutputStream out = new ObjectOutputStream(buffer)) {
      out.writeObject(t);
    }
    return buffer.toByteArray();
  }

  private static <T> T deserialize(byte[] data) throws IOException {
    try (InputStream buffer = new ByteArrayInputStream(data);
    ObjectInputStream in = new ObjectInputStream(buffer)) {
      T t = (T) in.readObject();
      return t;
    } catch (ClassNotFoundException e) {
      throw new ContractViolationException(e);
    }
  }
}
