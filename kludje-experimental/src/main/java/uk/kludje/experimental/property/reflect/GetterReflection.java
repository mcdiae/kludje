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

import java.lang.reflect.Method;

/**
 * A source of method references for working with methods.
 */
public final class GetterReflection {

  private static final String GETTER = "^get.+";
  private static final String ISSER = "^is.+";

  private GetterReflection() {}

  public static boolean excludeNothing(Method method) {
    return false;
  }

  public static boolean excludeNonBeanProperties(Method method) {
    String name = method.getName();
    return name.matches(GETTER)
      || (returnsBoolean(method) && name.matches(ISSER));
  }

  private static boolean returnsBoolean(Method method) {
    Class<?> returnType = method.getReturnType();
    return returnType == Boolean.TYPE
      || returnType == Boolean.class;
  }

  public static String methodToPropertyName(Method method) {

    String name = method.getName();
    int firstIndex;

    if (name.matches(GETTER)) {
      firstIndex = 4;
    } else if (name.matches(ISSER)) {
      firstIndex = 3;
    } else {
      throw new IllegalArgumentException("Method name '" + name + "' is not a recognised getter");
    }

    char first = name.charAt(firstIndex);
    char lower = Character.toLowerCase(first);
    return lower + name.substring(firstIndex);
  }
}
