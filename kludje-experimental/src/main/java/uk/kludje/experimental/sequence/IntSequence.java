package uk.kludje.experimental.sequence;

public abstract class IntSequence implements Sequence<Integer> {
  abstract int getInt(int index);

  @Override
  public Integer get(int index) {
    return getInt(index);
  }

  @Override
  public final boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof IntSequence)) {
      return false;
    }
    IntSequence that = (IntSequence) obj;
    return Contract.areEqual(this, that, i -> this.get(i) == that.get(i));
  }

  @Override
  public final int hashCode() {
    return Contract.hashOf(this, i -> getInt(i));
  }

  @Override
  public final String toString() {
    return Contract.string(this, this::hex);
  }

  private void hex(int i, StringBuilder buf) {
    buf.append(String.format("%08x", getInt(i) ));
  }
}
