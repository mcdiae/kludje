/*
 * Copyright 2015 McDowell
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.kludje.fn;

import uk.kludje.Exceptions;

import java.util.Objects;
import java.util.function.Function;

/**
 * A {@link java.util.function.Function} with null-safe checks.
 *
 * For use with getter chains where one or more elements in the chain
 * can be null.
 *
 * Example usage:
 * <pre>D d = Nullifier.span(A::getB, B::getC, C::getD).apply(a);</pre>
 *
 * Implement {@link #$apply(Object)}; invoke {@link #apply(Object)}.
 *
 * @param <T> the input
 * @param <R> the result
 */
@FunctionalInterface
public interface Nullifier<T, R> extends Function<T, R> {

  /**
   * If the argument is null, returns null; else invokes {@link #$apply(Object)}.
   *
   * This method rethrows any exception thrown by {@link #$apply(Object)} as an unchecked exception.
   *
   * @param t the input which may be null
   * @return the result which may be null
   * @see uk.kludje.Exceptions#throwChecked(Throwable)
   */
  @Override
  default R apply(T t) {
    try {
      return (t == null) ? null : $apply(t);
    } catch(Exception e) {
      throw Exceptions.throwChecked(e);
    }
  }

  /**
   * Implement this method with a lambda expression/method reference.
   * Consumers should invoke {@link #apply(Object)} and NOT call this method directly.
   *
   * @param t the argument
   * @return the result
   * @throws Exception on error
   */
  R $apply(T t) throws Exception;

  @Override
  default <V> Nullifier<V, R> compose(Function<? super V, ? extends T> before) {
    Objects.requireNonNull(before);
    return (V v) -> apply(before.apply(v));
  }

  @Override
  default <V> Nullifier<T, V> andThen(Function<? super R, ? extends V> after) {
    Objects.requireNonNull(after);
    return (T t) -> after.apply(apply(t));
  }

  /**
   * Creates a null-safe chain of function calls spanning two given functions.
   *
   * The functions may not be null, but the inputs and outputs may be.
   *
   * @param f0 the initial function; may not be null
   * @param f1 a subsequent function; may not be null
   * @param <A> the initial type
   * @param <B> an intermediary type
   * @param <Z> the resultant type
   * @return a function that, given A, returns Z, or null if any element in the chain is null
   */
  public static <A, B, Z> Nullifier<A, Z> span(Nullifier<A, B> f0, Nullifier<B, Z> f1) {
    return f0.andThen(f1);
  }

  public static <A, B, C, Z> Nullifier<A, Z> span(Nullifier<A, B> f0, Nullifier<B, C> f1, Nullifier<C, Z> f2) {
    return f0.andThen(f1)
        .andThen(f2);
  }

  public static <A, B, C, D, Z> Nullifier<A, Z> span(Nullifier<A, B> f0,
                                                     Nullifier<B, C> f1,
                                                     Nullifier<C, D> f2,
                                                     Nullifier<D, Z> f3) {
    return f0.andThen(f1)
        .andThen(f2)
        .andThen(f3);
  }

  public static <A, B, C, D, E, Z> Nullifier<A, Z> span(Nullifier<A, B> f0,
                                                        Nullifier<B, C> f1,
                                                        Nullifier<C, D> f2,
                                                        Nullifier<D, E> f3,
                                                        Nullifier<E, Z> f4) {
    return f0.andThen(f1)
        .andThen(f2)
        .andThen(f3)
        .andThen(f4);
  }
}
