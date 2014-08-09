package uk.kludje.experimental.sequence;

public abstract class ShortSequence implements Sequence<Short> {
  abstract short getShort(int index);

  @Override
  public Short get(int index) {
    return getShort(index);
  }

  @Override
  public final boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof ShortSequence)) {
      return false;
    }
    ShortSequence that = (ShortSequence) obj;
    return Contract.areEqual(this, that, i -> this.get(i) == that.get(i));
  }

  @Override
  public final int hashCode() {
    return Contract.hashOf(this, i -> Short.hashCode(getShort(i)));
  }

  @Override
  public final String toString() {
    return Contract.string(this, this::hex);
  }

  private void hex(int i, StringBuilder buf) {
    buf.append(String.format("%04x", getShort(i) & 0xFFFF));
  }
}
