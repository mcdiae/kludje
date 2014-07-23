/*
Copyright 2014 McDowell

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package uk.kludje.test;

import uk.kludje.Exceptions;

import java.util.function.Function;

public interface UFunction<T, R> extends Function<T, R> {

  default R apply(T t) {
    try {
      return applyChecked(t);
    } catch (Throwable e) {
      throw Exceptions.throwChecked(e);
    }
  }

  R applyChecked(T t) throws Throwable;
}
