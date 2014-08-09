/*
 *
 * Copyright $year McDowell
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
 * /
 */

package uk.kludje.experimental.sequence;

public abstract class BooleanSequence implements Sequence<Boolean> {
  public abstract boolean booleanAt(int index);

  @Override
  public Boolean get(int index) {
    return booleanAt(index);
  }

  @Override
  public final boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof BooleanSequence)) {
      return false;
    }
    BooleanSequence that = (BooleanSequence) obj;
    return Contract.areEqual(this, that, i -> this.get(i) == that.get(i));
  }

  @Override
  public final int hashCode() {
    return Contract.hashOf(this, i -> Boolean.hashCode(booleanAt(i)));
  }

  @Override
  public final String toString() {
    return Contract.string(this, (i, buf) -> buf.append(booleanAt(i) ? '1' : '0'));
  }
}
