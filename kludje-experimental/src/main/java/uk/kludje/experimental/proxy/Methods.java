/*
Copyright 2014 McDowell

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

package uk.kludje.experimental.proxy;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

final class Methods {

  private static final Class<?>[] EQUALS_PARAMS = {Object.class};
  private static final Map<Class<?>, Object> DEF_VALUES;

  static {
    Map<Class<?>, Object> map = new HashMap<>();
    map.put(Boolean.TYPE, false);
    map.put(Character.TYPE, '\0');
    map.put(Byte.TYPE, (byte) 0);
    map.put(Short.TYPE, (short) 0);
    map.put(Integer.TYPE, 0);
    map.put(Long.TYPE, 0L);
    map.put(Float.TYPE, 0.0f);
    map.put(Double.TYPE, 0.0d);
    DEF_VALUES = Collections.unmodifiableMap(map);
  }

  private Methods() {
  }

  public static Object[] defaultArgs(Method method) {
    return Arrays.asList(method.getParameterTypes())
        .stream()
        .map(DEF_VALUES::get)
        .toArray();
  }

  public static boolean isToString(Method m) {
    return m.getParameterTypes().length == 0
        && m.getName().equals("toString");
  }

  public static boolean isEquals(Method m) {
    return m.getName().equals("equals")
        && Arrays.equals(EQUALS_PARAMS, m.getParameterTypes());
  }

  public static boolean isHashCode(Method m) {
    return m.getName().equals("hashCode")
        && m.getParameterTypes().length == 0;
  }
}
