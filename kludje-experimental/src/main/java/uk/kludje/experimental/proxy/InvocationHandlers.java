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

import java.lang.invoke.MethodHandles;
import java.lang.reflect.InvocationHandler;

public final class InvocationHandlers {
  private InvocationHandlers() {
  }

  /**
   * @return handler that always returns the method's default value
   * @see java.lang.reflect.Method#getDefaultValue()
   */
  public static InvocationHandler defaultValueHandler() {
    return (proxy, method, args) -> method.getDefaultValue();
  }

  /**
   * @return handler that always throws UnsupportedOperationException
   * @see java.lang.UnsupportedOperationException
   */
  public static InvocationHandler unsupportedOperationHandler() {
    return (proxy, method, args) -> {
      throw new UnsupportedOperationException(method.toString());
    };
  }

  /**
   * Handler for invoking interface default methods.
   * This handler will throw an exception if the method is not a default method.
   *
   * @return handler for invoking interface default methods
   * @see java.lang.reflect.Method#isDefault()
   */
  public static InvocationHandler defaultMethodHandler() {
    return (proxy, method, args) -> {
      if (method.isDefault()) {
        return MethodHandles.lookup()
            .in(method.getDeclaringClass())
            .unreflectSpecial(method, method.getDeclaringClass())
            .bindTo(proxy)
            .invokeWithArguments(args);
      } else {
        throw new IllegalArgumentException(method.toString());
      }
    };
  }

  public static InvocationHandler objectMethodsHandler() {
    return (proxy, method, args) -> {
      if (Methods.isEquals(method)) {
        return proxy == args[0];
      }
      if (Methods.isHashCode(method)) {
        return Integer.MIN_VALUE;
      }
      if (Methods.isToString(method)) {
        return "Proxy[" + proxy.getClass() + "]";
      }
      throw new IllegalArgumentException(method.toString());
    };
  }
}
