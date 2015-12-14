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
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Represents an interface type.
 * <p>
 * Usage:
 * <pre>
 * //simple
 * Interface&lt;Runnable&gt; r = Interface.type(Runnable.class);
 * //generics
 * Interface&lt;List&lt;String&gt;&gt; l = new Interface&lt;List&lt;String&gt;&gt; {};
 * </pre>
 *
 * @param <T> a type which must be an interface
 */
public abstract class Interface<T> {
  private final Class<T> type;
  private volatile List<Method> defaultMethods;
  private volatile List<Method> abstractMethods;
  private volatile List<Method> staticMethods;

  protected Interface() {
    this.type = fromImpl();
  }

  private Interface(Class<T> type) {
    this.type = type;
  }

  /**
   * Usage: {@code Interface<Runnable> ir = Interface.type(Runnable.class);}
   *
   * @param anyInterface a non-null interface type
   * @param <T>          an interface
   * @return an instance encapsulating the argument
   */
  public static <T> Interface<T> type(Class<T> anyInterface) {
    class InterfaceType extends Interface<T> {
      InterfaceType() {
        super(anyInterface);
      }
    }

    Objects.requireNonNull(anyInterface, "anyInterface");
    Assert.ensure(anyInterface.isInterface(), "anyInterface.isInterface()");

    return new InterfaceType();
  }

  private Class<T> fromImpl() {
    Type thisType = getClass().getGenericSuperclass();
    if(!(thisType instanceof ParameterizedType)) {
      throw new IllegalImplementationError("expected " + thisType + " to be parameterized");
    }
    Object typeArguments = ((ParameterizedType) thisType).getActualTypeArguments()[0];
    Class<T> type;
    if(typeArguments instanceof Class<?>) {
      @SuppressWarnings("unchecked")
      Class<T> safe = (Class<T>) typeArguments;
      type = safe;
    } else if(typeArguments instanceof  ParameterizedType) {
      ParameterizedType parameterizedType = (ParameterizedType) typeArguments;
      @SuppressWarnings("unchecked")
      Class<T> safe = (Class<T>) parameterizedType.getRawType();
      type = safe;
    } else {
      throw new IllegalImplementationError("unable to resolve generic type for " + this);
    }
    if (!type.isInterface()) {
      throw new IllegalImplementationError("expected interface; got " + type);
    }
    return type;
  }

  public Class<T> type() {
    return type;
  }

  public List<Method> defaultMethods() {
    if (this.defaultMethods == null) {
      defaultMethods = listOf(Method::isDefault);
    }
    return defaultMethods;
  }

  public List<Method> abstractMethods() {
    if (abstractMethods == null) {
      abstractMethods = listOf(this::isAbstract);
    }
    return abstractMethods;
  }

  public List<Method> staticMethods() {
    if (staticMethods == null) {
      staticMethods = listOf(this::isStatic);
    }
    return staticMethods;
  }

  private boolean isAbstract(Method m) {
    return !m.isDefault() && !Modifier.isStatic(m.getModifiers());
  }

  private boolean isStatic(Method m) {
    return Modifier.isStatic(m.getModifiers());
  }

  private List<Method> listOf(Predicate<Method> test) {
    return Arrays.asList(type.getMethods())
        .stream()
        .filter(test)
        .collect(Collectors.toList());
  }

  /**
   * Indicates checked the type has not been subtyped correctly.
   */
  @SuppressWarnings("serial")
  private static class IllegalImplementationError extends Error {
    public IllegalImplementationError(String msg) {
      super(msg);
    }
  }
}
