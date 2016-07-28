package uk.kludje.property;

import uk.kludje.Ensure;

import java.util.Arrays;
import java.util.function.Function;

/**
 * A functional interface for reading a property value.
 *
 * @param <T> the type to read the property from
 */
@FunctionalInterface
public interface Getter<T> extends TypedProperty, Function<T, Object> {
  /**
   * Reads a property value from the argument.
   *
   * @param t the source of the property value
   * @return the property value instance or null
   */
  Object get(T t);

  /** @return OBJECT */
  default PropertyType type() {
    return PropertyType.OBJECT;
  }

  /**
   * Just invokes {@link #get(Object)}.
   *
   * @param t the source of the property value
   * @return the property value instance or null
   */
  @Override
  default Object apply(T t) {
    return get(t);
  }

  /**
   * Decorates the argument with an instance that throws an error if the argument returns an array of any type.
   *
   * @param getter the type to decorate; cannot be null
   * @param <T> the source type
   * @return a new instance
   */
  @Deprecated // not sure this is safe
  static <T> Getter<T> neverArray(Getter<T> getter) {
    Ensure.that(getter != null, "getter != null");

    class NeverArray implements Getter<T> {

      @Override
      public Object get(T t) {
        Object o = getter.get(t);
        Ensure.that(!(o instanceof Object[]), "!(o instanceof Object[])");
        Ensure.that(!(o instanceof boolean[]), "!(o instanceof boolean[])");
        Ensure.that(!(o instanceof char[]), "!(o instanceof char[])");
        Ensure.that(!(o instanceof byte[]), "!(o instanceof byte[])");
        Ensure.that(!(o instanceof short[]), "!(o instanceof short[])");
        Ensure.that(!(o instanceof int[]), "!(o instanceof int[])");
        Ensure.that(!(o instanceof long[]), "!(o instanceof long[])");
        Ensure.that(!(o instanceof float[]), "!(o instanceof float[])");
        Ensure.that(!(o instanceof double[]), "!(o instanceof double[])");
        return o;
      }
    }

    return new NeverArray();
  }

  /**
   * Decorates a getter with a return type that makes instance identity paramount.
   *
   * @param getter the instance to decorate
   * @param <T> the source type
   * @return the decorated instance
   * @see IdentityObject
   */
  @Deprecated // not sure this is safe
  static <T> Getter<T> identity(Getter<T> getter) {
    Ensure.that(getter != null, "getter != null");

    class Identity implements Getter<T> {
      @Override
      public Object get(T t) {
        Object result = getter.get(t);
        return new IdentityObject(result);
      }
    }

    return new Identity();
  }
}