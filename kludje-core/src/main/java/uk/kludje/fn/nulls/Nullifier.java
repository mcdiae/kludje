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

package uk.kludje.fn.nulls;

import uk.kludje.Exceptions;

import java.util.Objects;
import java.util.function.Function;

/**
 * An extension of {@link java.util.function.Function} that returns null.
 * For use with getter chains where one or more elements in the chain
 * can be null.
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
   * @return the result
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
   * Consumers should invoke {@link #apply(Object)}.
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

  default <V> Nullifier<V, R> checkCompose(Nullifier<? super V, ? extends T> before) {
    Objects.requireNonNull(before);
    return (V v) -> apply(before.apply(v));
  }

  @Override
  default <V> Nullifier<T, V> andThen(Function<? super R, ? extends V> after) {
    Objects.requireNonNull(after);
    return (T t) -> after.apply(apply(t));
  }

  default <V> Nullifier<T, V> andThenCheck(Nullifier<? super R, ? extends V> after) {
    Objects.requireNonNull(after);
    return (T t) -> after.apply(apply(t));
  }

  public static <A, B, Z> Nullifier<A, Z> check(Nullifier<A, B> f0, Nullifier<B, Z> f1) {
    return f0.andThen(f1);
  }

  public static <A, B, C, Z> Nullifier<A, Z> check(Nullifier<A, B> f0, Nullifier<B, C> f1, Nullifier<C, Z> f2) {
    return f0.andThen(f1)
        .andThen(f2);
  }

  public static <A, B, C, D, Z> Nullifier<A, Z> check(Nullifier<A, B> f0,
                                                      Nullifier<B, C> f1,
                                                      Nullifier<C, D> f2,
                                                      Nullifier<D, Z> f3) {
    return f0.andThen(f1)
        .andThen(f2)
        .andThen(f3);
  }
}
