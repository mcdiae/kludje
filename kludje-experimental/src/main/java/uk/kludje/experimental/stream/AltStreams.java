package uk.kludje.experimental.stream;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public final class AltStreams {

  private AltStreams() {}

  /**
   * @param bytes the source data
   * @return a stream of signed ints from the source bytes
   * @see Byte#MAX_VALUE
   * @see Byte#MAX_VALUE
   */
  public static IntStream bytes(byte...bytes) {
    return IntStream.range(0, bytes.length)
      .map(index -> bytes[index]);
  }

  /**
   * @param chars the source data
   * @return a stream of ints from the source chars
   * @see Character#MIN_VALUE
   * @see Character#MAX_VALUE
   */
  public static IntStream chars(char...chars) {
    return IntStream.range(0, chars.length)
      .map(index -> chars[index]);
  }

  /**
   * Consumes data from the supplier until a condition is met.
   * Use this method where a "poison pill" can be supplied to stop processing.
   *
   * @param supplier element source
   * @param stop stops invoking supplier when this returns true
   * @param <E> the element type
   * @return a new stream
   */
  public static <E> Stream<E> streamUntil(Supplier<? extends E> supplier, Predicate<? super E> stop) {

    Iterator<E> iterator = AltIterators.iterateUntil(supplier, stop);
    Spliterator<E> spliterator = Spliterators.spliterator(iterator, Long.MAX_VALUE, Spliterator.ORDERED);
    return StreamSupport.stream(spliterator, false);
  }
}
