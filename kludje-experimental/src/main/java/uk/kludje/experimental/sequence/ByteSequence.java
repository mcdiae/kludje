package uk.kludje.experimental.sequence;

public abstract class ByteSequence implements Sequence<Byte> {
  public abstract byte byteAt(int index);

  @Override
  public Byte get(int index) {
    return byteAt(index);
  }

  @Override
  public final boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof ByteSequence)) {
      return false;
    }
    ByteSequence that = (ByteSequence) obj;
    return Contract.areEqual(this, that, i -> this.get(i) == that.get(i));
  }

  @Override
  public final int hashCode() {
    return Contract.hashOf(this, i -> Byte.hashCode(byteAt(i)));
  }

  @Override
  public final String toString() {
    return Contract.string(this, this::hex);
  }

  private void hex(int i, StringBuilder buf) {
    buf.append(String.format("%02x", byteAt(i) & 0xFF));
  }
}
