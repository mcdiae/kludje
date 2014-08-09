package uk.kludje.test;

import uk.kludje.Meta;

import static uk.kludje.Meta.meta;

public class GenericPojo<T> {
  private static final Meta<GenericPojo> META = meta(GenericPojo.class)
      .objects($ -> $.gen);

  private final T gen;

  public GenericPojo(T gen) {
    this.gen = gen;
  }

  @Override
  public boolean equals(Object obj) {
    return META.equals(this, obj);
  }

  @Override
  public int hashCode() {
    return META.hashCode(this);
  }

  @Override
  public String toString() {
    return META.toString(this);
  }
}
