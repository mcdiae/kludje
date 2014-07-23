package uk.kludje.proxy;

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
