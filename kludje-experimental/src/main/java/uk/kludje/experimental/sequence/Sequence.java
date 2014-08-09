package uk.kludje.experimental.sequence;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface Sequence<T> extends Iterable<T> {
  int length();

  T get(int index);

  default IntStream indices() {
    return IntStream.range(0, length());
  }

  default Stream<T> stream() {
    return StreamSupport.stream(this.spliterator(), false);
  }

  @Override
  default Iterator<T> iterator() {
    return new SequenceIterator<>(this);
  }
}

final class SequenceIterator<E> implements Iterator<E> {
  private final Sequence<E> sequence;
  private int index = 0;

  public SequenceIterator(Sequence<E> seq) {
    this.sequence = seq;
  }

  @Override
  public boolean hasNext() {
    return index < sequence.length();
  }

  @Override
  public E next() {
    if (!hasNext()) {
      throw new NoSuchElementException();
    }
    return sequence.get(index++);
  }
}