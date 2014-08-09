package uk.kludje.experimental.sequence;

public abstract class BooleanSequence implements Sequence<Boolean> {
  public abstract boolean booleanAt(int index);

  @Override
  public Boolean get(int index) {
    return booleanAt(index);
  }

  @Override
  public final boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof BooleanSequence)) {
      return false;
    }
    BooleanSequence that = (BooleanSequence) obj;
    return Contract.areEqual(this, that, i -> this.get(i) == that.get(i));
  }

  @Override
  public final int hashCode() {
    return Contract.hashOf(this, i -> Boolean.hashCode(booleanAt(i)));
  }

  @Override
  public final String toString() {
    return Contract.string(this, (i, buf) -> buf.append(booleanAt(i) ? '1' : '0'));
  }
}
