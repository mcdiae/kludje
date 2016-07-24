package uk.kludje.property;

import java.util.Objects;

/**
 * Does equality based on instance identity.
 */
final class IdentityObject {

  private final Object o;

  public IdentityObject(Object o) {
    this.o = o;
  }

  @Override
  public boolean equals(Object o1) {
    return o1 instanceof IdentityObject
      && ((IdentityObject) o1).o == o;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(o);
  }

  @Override
  public String toString() {
    return (o == null) ? "null" : o.toString();
  }
}
