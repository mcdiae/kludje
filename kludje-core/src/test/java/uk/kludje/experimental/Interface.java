package uk.kludje.experimental;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
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
    Object typeArguments = ((ParameterizedType) getClass()
        .getGenericSuperclass()).getActualTypeArguments()[0];
    if (!(typeArguments instanceof Class<?>)) {
      throw new IllegalImplementationError("expected class type argument; got " + typeArguments);
    }
    Class<T> type = (Class<T>) typeArguments;
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

  private static class IllegalImplementationError extends Error {
    public IllegalImplementationError(String msg) {
      super(msg);
    }
  }
}
