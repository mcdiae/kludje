package uk.kludje.property;

@FunctionalInterface
public interface ShortGetter<T> extends TypedProperty {
  short getShort(T t);

  /** @return SHORT */
  default PropertyType type() {
    return PropertyType.SHORT;
  }
}
