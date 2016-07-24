package uk.kludje.property;

@FunctionalInterface
public interface CharGetter<T> extends TypedProperty {
  char getChar(T t);

  /** @return CHAR */
  default PropertyType type() {
    return PropertyType.CHAR;
  }
}
