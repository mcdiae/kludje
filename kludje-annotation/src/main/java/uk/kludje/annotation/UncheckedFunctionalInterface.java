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

package uk.kludje.annotation;

import java.lang.annotation.*;

/**
 * Annotation for generating functional interfaces that handle checked exceptions.
 * <p>
 * Example usage:
 *
 * <pre>
 *   // declared in foo/package-info.java
 *   {@literal @UncheckedFunctionalInterface(ToLongFunction.class) }
 *   {@literal @UncheckedFunctionalInterface(UnaryOperator.class) }
 *   package foo;
 * </pre>
 *
 * When used with {@link uk.kludje.annotation.processor.UncheckedFunctionalInterfaceProcessor} will generated
 * functional interfaces for use in lambda expressions. These new interfaces throw checked exceptions as
 * unchecked exceptions.
 *
 * @see UncheckedFunctionalInterfaces
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.PACKAGE})
@Repeatable(UncheckedFunctionalInterfaces.class)
public @interface UncheckedFunctionalInterface {

  /**
   * Values must be a type that is a functional interface.
   *
   * @return the value type
   * @see FunctionalInterface
   */
  Class<?> value();
}
