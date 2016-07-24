package uk.kludje.property;

@FunctionalInterface
public interface FloatGetter<T> extends TypedProperty {
  float getFloat(T t);

  /** @return FLOAT */
  default PropertyType type() {
    return PropertyType.FLOAT;
  }
}
