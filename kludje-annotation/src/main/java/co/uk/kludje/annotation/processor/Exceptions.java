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

package co.uk.kludje.annotation.processor;

final class Exceptions {
  private Exceptions() {
  }

  /**
   * Throws any type of @{link Throwable} as an unchecked type.
   *
   * @param t the (non-null) type to throw
   */
  public static void throwChecked(Throwable t) {
    if (t == null) {
      throw new AssertionError("null");
    }
    Exceptions.<RuntimeException>throwIt(t);
  }

  @SuppressWarnings("unchecked")
  private static <T extends Throwable> void throwIt(Throwable t) throws T {
    throw (T) t;
  }

  public static interface Throws {
    public <T extends Throwable> Throws expected() throws T;
  }

  private static class ThrowsImpl implements Throws {
    private static final Throws INSTANCE = new ThrowsImpl();

    @Override
    public <T extends Throwable> Throws expected() throws T {
      return INSTANCE;
    }
  }

  public static <T extends Throwable> Throws expected() throws T {
    return ThrowsImpl.INSTANCE;
  }
}