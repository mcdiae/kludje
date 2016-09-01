package uk.kludje.experimental.property.reflect;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

final class ReflectionGetterFinder {

  private ReflectionGetterFinder() {}

  public static List<Method> findGetters(Class<?> type, Predicate<Method> excluder) {

    List<Method> results = new ArrayList<>();

    for (Method method : type.getMethods()) {

      String name = method.getName();
      int mods = method.getModifiers();

      if (excluder.test(method)
        || (method.getParameterCount() > 0)
        || Modifier.isStatic(mods)
        || (!Modifier.isPublic(mods))
        || "toString".equals(name)
        || "getClass".equals(name)) {
        continue;
      }

      results.add(method);
    }

    return results;
  }

}
