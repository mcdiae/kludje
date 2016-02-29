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

package uk.kludje.collect.array;

import uk.kludje.array.EmptyArrays;
import uk.kludje.array.LinearSearch;

import java.util.*;

/**
 * Created by user on 13/12/15.
 */
final class ArrayBackedMutableMap<K, V> extends MutableStore implements Map<K, V> {

  private Object[] keys = EmptyArrays.EMPTY_OBJECT_ARRAY;

  @Override
  public boolean containsKey(Object key) {
    return get(key) != null;
  }

  @Override
  public boolean containsValue(Object value) {
    return LinearSearch.find(elements, start, end, value) != LinearSearch.NOT_FOUND;
  }

  @Override
  public V get(Object key) {
    int index = LinearSearch.find(keys, start, end, key);
    if (index == LinearSearch.NOT_FOUND) {
      return null;
    }
    @SuppressWarnings("unchecked")
    V value = (V) elements[index];
    return value;
  }

  @Override
  public V put(K key, V value) {
    assert key != this: "key != this";
    assert value != this: "value != this";

    int index = LinearSearch.find(keys, start, end, key);
    if (index != LinearSearch.NOT_FOUND) {
      @SuppressWarnings("unchecked")
      V old = (V) elements[index];
      elements[index] = value;
      return old;
    }
    if (start > 0) {
      keys[--start] = key;
      elements[start] = value;
      return null;
    }
    ensureFreeTailCapacity(1);

    return null;
  }

  protected void ensureFreeTailCapacity(int n) {
    assert n >= 0: "n >= 0";

    version++;

    int endCapacity = elements.length - end;
    if (n <= endCapacity) {
      // have enough room
      return;
    }

    int size = size();
    int capacity = endCapacity + start;
    if (n <= capacity) {
      defrag();
      // make room at the end
      return;
    }
    // increase space
    int increase = growSizeBy(n - size);
    int newSize = increase + size;
    Assert.that(newSize >= (size + n), "newSize >= (size + n)");
    Object[] newValues = new Object[newSize];
    Object[] newKeys = new Object[newSize];
    System.arraycopy(elements, start, newValues, 0, size);
    System.arraycopy(keys, start, newKeys, 0, size);
    start = 0;
    end = size;
    elements = newValues;
    keys = newKeys;
  }

  protected void defrag() {
    version++;
    int size = size();
    System.arraycopy(keys, start, keys, 0, size);
    System.arraycopy(elements, start, elements, 0, size);
    start = 0;
    end = size;
  }

  @Override
  public V remove(Object key) {
    int index = LinearSearch.find(keys, start, end, key);
    if (index == LinearSearch.NOT_FOUND) {
      return null;
    }
    return removeAtIndex(index);
  }

  private V removeAtIndex(int index) {
    @SuppressWarnings("unchecked")
    V old = (V) elements[index];
    if (index == start) {
      keys[start] = null;
      elements[start++] = null;
      return old;
    }
    int from = index + 1;
    int len = end - index;
    System.arraycopy(keys, from, keys, index, len);
    System.arraycopy(elements, from, elements, index, len);
    keys[--end] = null;
    elements[end] = null;
    return old;
  }

  @Override
  public void putAll(Map<? extends K, ? extends V> m) {
    for (Entry<? extends K, ? extends V> entry : m.entrySet()) {
      put(entry.getKey(), entry.getValue());
    }
  }

  @Override
  public MapKeySet<K> keySet() {
    return new KeySet();
  }

  @Override
  public MapValues<V> values() {
    return new MapValuesCollection();
  }

  @Override
  public MapEntrySet<K, V> entrySet() {
    return new EntrySet();
  }

  @Override
  public void clear() {
    clearElements();
  }

  @Override
  protected void clearElements() {
    Arrays.fill(keys, start, end, null);
    Arrays.fill(elements, start, end, null);
    start = 0;
    end = 0;
  }

  @Override
  public int size() {
    return super.size();
  }

  @Override
  public boolean isEmpty() {
    return super.isEmpty();
  }

  @Override
  public boolean equals(Object obj) {
    return (this == obj)
        || ((obj instanceof Map)
        && isEqual((Map<?, ?>) obj));
  }

  private boolean isEqual(Map<?, ?> that) {
    if (that.size() != size()) {
      return false;
    }

    for (int i = start; i < end; i++) {
      Object key = keys[i];
      Object thisValue = elements[i];
      Object thatValue = that.get(key);
      if (!Objects.equals(thisValue, thatValue)) {
        return false;
      }
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    for (int i = start; i < end; i++) {
      Object key = keys[i];
      Object value = elements[i];
      hash += Objects.hashCode(key) ^ Objects.hashCode(value);
    }
    return hash;
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder("{");
    String delimiter = "";
    for (int i = start; i < end; i++) {
      buf.append(delimiter)
          .append(keys[i])
          .append('=')
          .append(elements[i]);
      delimiter = ", ";
    }
    return buf.append("}").toString();
  }

  private class MapValuesCollection extends AbstractCollection<V> implements MapValues<V> {

    @Override
    public Iterator<V> iterator() {
      return new ValueIterator();
    }

    @Override
    public int size() {
      return ArrayBackedMutableMap.this.size();
    }
  }

  private class ValueIterator implements Iterator<V> {
    private final int version = ArrayBackedMutableMap.this.version;

    private int index = start;

    @Override
    public boolean hasNext() {
      checkVersion(this.version);
      return index < end;
    }

    @Override
    public V next() {
      checkVersion(this.version);
      if (!hasNext()) {
        throw new NoSuchElementException();
      }
      @SuppressWarnings("unchecked")
      V value = (V) elements[index++];
      return value;
    }
  }

  private class KeySet extends AbstractSet<K> implements MapKeySet<K> {
    @Override
    public Iterator<K> iterator() {
      return new KeyIterator();
    }

    @Override
    public int size() {
      return ArrayBackedMutableMap.this.size();
    }

    @Override
    public boolean remove(Object key) {
      int index = LinearSearch.find(keys, start, end, key);
      if (index == LinearSearch.NOT_FOUND) {
        return false;
      }
      removeAtIndex(index);
      return true;
    }

    @Override
    @Deprecated
    public boolean add(K k) {
      return super.add(k);
    }

    @Override
    @Deprecated
    public boolean addAll(Collection<? extends K> c) {
      return super.addAll(c);
    }
  }

  private class KeyIterator implements Iterator<K> {
    private int version = ArrayBackedMutableMap.this.version;

    private int index = start;

    @Override
    public boolean hasNext() {
      checkVersion(this.version);
      return index < end;
    }

    @Override
    public K next() {
      checkVersion(this.version);
      if (!hasNext()) {
        throw new NoSuchElementException();
      }
      @SuppressWarnings("unchecked")
      K key = (K) keys[index++];
      return key;
    }

    @Override
    public void remove() {
      checkVersion(this.version);
      if (index == start) {
        throw new IllegalStateException();
      }
      removeAtIndex(index - 1);
      this.version = ArrayBackedMutableMap.this.version;
    }
  }

  private class MapEntry extends AbstractMapEntry<K, V> {
    private MapEntry(K key, V value) {
      super(key, value);
    }

    @Override
    public V setValue(V value) {
      int index = LinearSearch.find(keys, start, end, key);
      if (index == LinearSearch.NOT_FOUND) {
        throw new IllegalStateException(key + " not a member of map");
      }
      V old = this.value;
      this.value = value;
      elements[index] = value;
      return old;
    }
  }

  private class EntryIterator implements Iterator<Entry<K, V>> {
    private int version = ArrayBackedMutableMap.this.version;

    private int index = start;

    @Override
    public boolean hasNext() {
      checkVersion(this.version);
      return index < end;
    }

    @Override
    public Entry<K, V> next() {
      checkVersion(this.version);
      if (!hasNext()) {
        throw new NoSuchElementException();
      }
      @SuppressWarnings("unchecked")
      K key = (K) keys[index];
      @SuppressWarnings("unchecked")
      V value = (V) elements[index++];
      return new MapEntry(key, value);
    }

    @Override
    public void remove() {
      checkVersion(this.version);
      if (index == start) {
        throw new IllegalStateException();
      }
      removeAtIndex(index - 1);
      this.version = ArrayBackedMutableMap.this.version;
    }
  }

  private class EntrySet extends AbstractSet<Entry<K, V>> implements MapEntrySet<K, V> {

    @Override
    public Iterator<Entry<K, V>> iterator() {
      return new EntryIterator();
    }

    @Override
    public int size() {
      return ArrayBackedMutableMap.this.size();
    }

    @Override
    public boolean remove(Object o) {
      if (o instanceof Entry<?, ?>) {
        Entry<?, ?> entry = (Entry<?, ?>) o;
        Object key = entry.getKey();
        int index = LinearSearch.find(keys, start, end, key);
        if (index != LinearSearch.NOT_FOUND
            && Objects.equals(elements[index], entry.getValue())) {
          removeAtIndex(index);
          return true;
        }
      }
      return false;
    }

    @Override
    @Deprecated
    public boolean add(Entry<K, V> kvEntry) {
      return super.add(kvEntry);
    }

    @Override
    @Deprecated
    public boolean addAll(Collection<? extends Entry<K, V>> c) {
      return super.addAll(c);
    }
  }
}
