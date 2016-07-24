package uk.kludje.property;

@FunctionalInterface
public interface DoubleGetter<T> extends TypedProperty {
  double getDouble(T t);

  /** @return DOUBLE */
  default PropertyType type() {
    return PropertyType.DOUBLE;
  }
}
