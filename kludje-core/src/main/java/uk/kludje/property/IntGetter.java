package uk.kludje.property;

@FunctionalInterface
public interface IntGetter<T> extends TypedProperty {
  int getInt(T t);

  /** @return INT */
  default PropertyType type() {
    return PropertyType.INT;
  }
}
