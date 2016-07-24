package uk.kludje.property;

@FunctionalInterface
public interface BooleanGetter<T> extends TypedProperty {
  boolean getBoolean(T t);

  /** @return BOOLEAN */
  default PropertyType type() {
    return PropertyType.BOOLEAN;
  }
}
