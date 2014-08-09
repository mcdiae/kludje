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

public abstract class CharacterSequence implements Sequence<Character>, CharSequence {
  @Override
  public Character get(int index) {
    return charAt(index);
  }

  @Override
  public final boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof CharacterSequence)) {
      return false;
    }
    CharacterSequence that = (CharacterSequence) obj;
    return Contract.areEqual(this, that, i -> this.get(i) == that.get(i));
  }

  /**
   * @see Sequences#charView(CharSequence, int, int)
   */
  @Override
  public CharacterSequence subSequence(int start, int end) {
    return Sequences.charView(this, start, end);
  }

  @Override
  public final int hashCode() {
    return Contract.hashOf(this, i -> charAt(i));
  }

  @Override
  public final String toString() {
    return Contract.string(this, (i, buf) -> buf.append(charAt(i)));
  }

  public final String toHexString() {
    return Contract.string(this, this::hex);
  }

  private void hex(int i, StringBuilder buf) {
    buf.append(String.format("%02x", charAt(i) & 0xFFFF));
  }
}
