package uk.kludje.property;

public abstract class PropertyGetterList<T, PGL extends PropertyGetterList<T, PGL>> {

  protected abstract PGL newInstance(PGL old, String name, TypedProperty getter);

  public abstract int size();

  public abstract String nameAt(int index);

  public abstract TypedProperty propertyAt(int index);

  public PGL namedObject(String name, Getter<T> getter) {
    return newInstance((PGL) this, name, getter);
  }

  public PGL namedBoolean(String name, BooleanGetter<T> getter) {
    return newInstance((PGL) this, name, getter);
  }

  public PGL namedByte(String name, ByteGetter<T> getter) {
    return newInstance((PGL) this, name, getter);
  }

  /**
   * @param name   a property name for toString() generation; must not be null
   * @param getter the value retriever
   * @return a new instance with the property appended
   */
  public PGL namedShort(String name, uk.kludje.property.ShortGetter<T> getter) {
    return newInstance((PGL) this, name, getter);
  }

  /**
   * @param name   a property name for toString() generation; must not be null
   * @param getter the value retriever
   * @return a new instance with the property appended
   */
  public PGL namedInt(String name, uk.kludje.property.IntGetter<T> getter) {
    return newInstance((PGL) this, name, getter);
  }

  /**
   * @param name   a property name for toString() generation; must not be null
   * @param getter the value retriever
   * @return a new instance with the property appended
   */
  public PGL namedLong(String name, uk.kludje.property.LongGetter<T> getter) {
    return newInstance((PGL) this, name, getter);
  }

  /**
   * @param name   a property name for toString() generation; must not be null
   * @param getter the value retriever
   * @return a new instance with the property appended
   */
  public PGL namedFloat(String name, uk.kludje.property.FloatGetter<T> getter) {
    return newInstance((PGL) this, name, getter);
  }

  /**
   * @param name   a property name for toString() generation; must not be null
   * @param getter the value retriever
   * @return a new instance with the property appended
   */
  public PGL namedDouble(String name, uk.kludje.property.DoubleGetter<T> getter) {
    return newInstance((PGL) this, name, getter);
  }

  /**
   * @param name   a property name for toString() generation; must not be null
   * @param getter the value retriever
   * @return a new instance with the property appended
   */
  public PGL namedChar(String name, uk.kludje.property.CharGetter<T> getter) {
    return newInstance((PGL) this, name, getter);
  }

  /**
   * Use to specify properties of type object checked should be considered by this type.
   * <p>
   * Do not use this method for primitive properties - alternatives have been provided.
   * <p>
   * This method does not mutate the instance; it returns a new one.
   *
   * @param getters a vararg array of non-null getters
   * @return a new instance
   */
  @SafeVarargs
  public final PGL objects(uk.kludje.property.Getter<T>... getters) {
    PGL result = (PGL) this;
    for (uk.kludje.property.Getter<T> g : getters) {
      result = newInstance(result, "", g);
    }
    return result;
  }

  @SafeVarargs
  public final PGL booleans(uk.kludje.property.BooleanGetter<T>... getters) {
    PGL result = (PGL) this;
    for (uk.kludje.property.BooleanGetter<T> g : getters) {
      result = newInstance(result, "", g);
    }
    return result;
  }

  @SafeVarargs
  public final PGL chars(uk.kludje.property.CharGetter<T>... getters) {
    PGL result = (PGL) this;
    for (uk.kludje.property.CharGetter<T> g : getters) {
      result = newInstance(result, "", g);
    }
    return result;
  }

  @SafeVarargs
  public final PGL bytes(uk.kludje.property.ByteGetter<T>... getters) {
    PGL result = (PGL) this;
    for (uk.kludje.property.ByteGetter<T> g : getters) {
      result = newInstance(result, "", g);
    }
    return result;
  }

  @SafeVarargs
  public final PGL shorts(uk.kludje.property.ShortGetter<T>... getters) {
    PGL result = (PGL) this;
    for (uk.kludje.property.ShortGetter<T> g : getters) {
      result = newInstance(result, "", g);
    }
    return result;
  }

  @SafeVarargs
  public final PGL ints(uk.kludje.property.IntGetter<T>... getters) {
    PGL result = (PGL) this;
    for (uk.kludje.property.IntGetter<T> g : getters) {
      result = newInstance(result, "", g);
    }
    return result;
  }

  @SafeVarargs
  public final PGL longs(uk.kludje.property.LongGetter<T>... getters) {
    PGL result = (PGL) this;
    for (uk.kludje.property.LongGetter<T> g : getters) {
      result = newInstance(result, "", g);
    }
    return result;
  }

  @SafeVarargs
  public final PGL floats(uk.kludje.property.FloatGetter<T>... getters) {
    PGL result = (PGL) this;
    for (uk.kludje.property.FloatGetter<T> g : getters) {
      result = newInstance(result, "", g);
    }
    return result;
  }

  @SafeVarargs
  public final PGL doubles(uk.kludje.property.DoubleGetter<T>... getters) {
    PGL result = (PGL) this;
    for (uk.kludje.property.DoubleGetter<T> g : getters) {
      result = newInstance(result, "", g);
    }
    return result;
  }
}
