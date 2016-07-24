package uk.kludje.property;

@FunctionalInterface
public interface LongGetter<T> extends TypedProperty {
  long getLong(T t);

  /** @return LONG */
  default PropertyType type() {
    return PropertyType.LONG;
  }
}
