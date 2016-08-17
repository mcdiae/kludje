package uk.kludje.experimental.stream;

import uk.kludje.Ensure;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Created by user on 17/08/16.
 */
class AltIterators {

  public static <E> Iterator<E> iterateUntil(Supplier<? extends E> supplier, Predicate<? super E> stop) {
    Ensure.that(supplier != null, "supplier != null");
    Ensure.that(stop != null, "stop != null");

    class UntilIterator implements Iterator<E> {

      E next = supplier.get();

      @Override
      public boolean hasNext() {
        return stop.test(next);
      }

      @Override
      public E next() {
        if (!hasNext()) {
          throw new NoSuchElementException("Stopped on value '" + next + "'");
        }

        try {
          return next;
        } finally {
          next = supplier.get();
        }
      }
    }

    return new UntilIterator();
  }
}
