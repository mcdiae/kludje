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
