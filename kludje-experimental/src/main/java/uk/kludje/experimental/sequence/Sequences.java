package uk.kludje.experimental.sequence;

// TODO: validation
public final class Sequences {
  private Sequences() {
  }

  public static BooleanSequence booleans(boolean... seq) {
    class ArrayBooleanSequence extends BooleanSequence {

      @Override
      public int length() {
        return seq.length;
      }

      @Override
      public boolean booleanAt(int index) {
        return seq[index];
      }
    }

    return new ArrayBooleanSequence();
  }

  public static ByteSequence bytes(byte... seq) {
    class ArrayByteSequence extends ByteSequence {

      @Override
      public int length() {
        return seq.length;
      }

      @Override
      public byte byteAt(int index) {
        return seq[index];
      }
    }

    return new ArrayByteSequence();
  }

  public static CharacterSequence chars(char... seq) {
    class ArrayCharSequence extends CharacterSequence {

      @Override
      public int length() {
        return seq.length;
      }

      @Override
      public char charAt(int index) {
        return seq[index];
      }
    }

    return new ArrayCharSequence();
  }

  public static CharacterSequence charView(CharSequence seq, int start, int end) {
    class CharView extends CharacterSequence {

      @Override
      public int length() {
        return end - start;
      }

      @Override
      public char charAt(int index) {
        return seq.charAt(index + start);
      }
    }

    return new CharView();
  }

  public static CharacterSequence charView(CharSequence seq) {
    return charView(seq, 0, seq.length());
  }
}
