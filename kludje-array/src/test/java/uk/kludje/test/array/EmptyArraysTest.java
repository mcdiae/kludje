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

package uk.kludje.test.array;

import org.junit.Assert;
import org.junit.Test;
import uk.kludje.array.EmptyArrays;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class EmptyArraysTest {

  @Test
  public void testEmptyArraySources() throws IllegalAccessException {
    testType(EmptyArrays.class);
  }

  private void testType(Class<?> c) throws IllegalAccessException {
    boolean tested = false;

    for (Field f : c.getFields()) {
      int modifiers = f.getModifiers();
      if (Modifier.isStatic(modifiers) && Modifier.isPublic(modifiers)) {
        testField(f);
        tested = true;
      }
    }

    Assert.assertTrue(tested);
  }

  private void testField(Field field) throws IllegalAccessException {
    Object value = field.get(null);
    int len = Array.getLength(value);
    Assert.assertEquals(field.toString(), 0, len);
  }
}
