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

package uk.kludje;

/**
 * Instance for when an arbitrary type and value is required.
 *
 * Usually, for when you want to use {@code Void} in generics but don't want {@code null} values or arbitrary
 * instances of {@code Void}.
 */
public enum Nil {
  /**The one and only instance of {@code Nil}*/
  NIL
}
