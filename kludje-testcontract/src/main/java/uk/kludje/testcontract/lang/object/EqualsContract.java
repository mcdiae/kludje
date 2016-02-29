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

package uk.kludje.testcontract.lang.object;

import java.util.function.Supplier;

import static uk.kludje.testcontract.ContractViolationException.assertThat;

public final class EqualsContract {

  private EqualsContract() {}

  public static void assertNotNull(Object x) {
    Supplier<Object> nullSupplier = () -> null;
    assertThat(!x.equals(nullSupplier.get()), "assertReflexive: " + x.getClass());
  }

  public static void assertReflexive(Object x) {
    assertThat(x.equals(x), "assertReflexive: " + x.getClass());
  }

  public static void assertSymmetric(Object x, Object y) {
    assertThat(x.equals(y) == y.equals(x), "assertSymmetric: " + x.getClass() + ";" + y.getClass());
  }

  public static void assertConsistent(Object x, Object y) {
    assertThat(x.equals(y) == x.equals(y), "assertConsistent: " + x.getClass() + ";" + y.getClass());
  }

  public static void assertTransitive(Object x, Object y, Object z) {
    boolean xEqualsY = x.equals(y);
    boolean xEqualsZ = x.equals(z);
    if (xEqualsY && xEqualsZ) {
      assertThat(y.equals(z), "assertTransitive: " + x.getClass() + ";" + y.getClass() + ";" + z.getClass());
    } else if (xEqualsY || xEqualsZ) {
      assertThat(!y.equals(z), "assertTransitive: " + x.getClass() + ";" + y.getClass() + ";" + z.getClass());
    }
  }

  public static void assertHashCode(Object x, Object y) {
    if(x.equals(y)) {
      assertThat(x.hashCode() == y.hashCode(), "assertHashCode: " + x.getClass() + ";" + y.getClass());
    }
  }

  public static void assertAll(Object x, Object y, Object z) {
    assertReflexive(x);
    assertConsistent(x, y);
    assertHashCode(x, y);
    assertNotNull(x);
    assertSymmetric(x, y);
    assertTransitive(x, y, z);
  }
}
