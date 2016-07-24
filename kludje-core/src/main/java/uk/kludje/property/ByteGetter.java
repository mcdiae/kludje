package uk.kludje.property;

@FunctionalInterface
public interface ByteGetter<T> extends TypedProperty {
  byte getByte(T t);

  /** @return BYTE */
  default PropertyType type() {
    return PropertyType.BYTE;
  }
}
