package uk.kludje;

import java.lang.reflect.Array;

/**
 * An array manipulation type.
 * Instances of this type are immutable and thread-safe.
 *
 * @param <T> the type of the array
 */
final class ArrayMapper<T> {

  /** A string array mapper. */
  private static final ArrayMapper<String> STRING_ARRAY_MAPPER = new ArrayMapper<>(String.class);

  private final Class<T> type;
  private final T[] empty;

  @SuppressWarnings("unchecked")
  private ArrayMapper(Class<T> type) {
    Fatal.when(type == null, "type == null");

    this.type = type;
    this.empty = (T[]) Array.newInstance(type, 0);
  }

  /**
   * @param type the array class
   * @param <T> the generic type
   * @return an instance for the given type; some instances may be interned
   */
  @SuppressWarnings("unchecked")
  public static <T> ArrayMapper<T> arrayMapper(Class<T> type) {
    if (type == String.class) {
      return (ArrayMapper<T>) STRING_ARRAY_MAPPER;
    }

    return new ArrayMapper<>(type);
  }

  /**
   * Returns an empty array of type {@code T}.
   * This method will always return the same instance for a given instance of {@link ArrayMapper}.
   *
   * @return an array with length zero
   */
  public T[] empty() {
    return empty;
  }

  /**
   * Returns a new instance with the given element at the end.
   *
   * @param oldArray the old array
   * @param newElement the element to contatenate
   * @param <E> the element type
   * @return a new array
   */
  @SuppressWarnings("unchecked")
  public <E extends T> T[] concat(T[] oldArray, E newElement) {
    T[] arr = (T[]) Array.newInstance(type, oldArray.length + 1);
    System.arraycopy(oldArray, 0, arr, 0, oldArray.length);
    arr[oldArray.length] = newElement;
    return  arr;
  }
}
