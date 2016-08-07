package uk.kludje.property;

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
}