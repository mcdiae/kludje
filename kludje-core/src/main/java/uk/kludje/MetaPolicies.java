package uk.kludje;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.ToIntFunction;

final class MetaPolicies {

  static final Meta.ObjectEqualsPolicy DEFAULT_EQUALS_POLICY = Objects::equals;
  static final Meta.ObjectHashCodePolicy DEFAULT_HASHCODE_POLICY = Objects::hashCode;

  private MetaPolicies() {}

  static final class SameClassCheck implements Meta.InstanceCheckPolicy {

    static final Meta.InstanceCheckPolicy INSTANCE = new SameClassCheck();

    @Override
    public boolean isSameType(Class<?> declaredType, Object thatInstance) {
      return declaredType == thatInstance.getClass();
    }
  }

  static final class InstanceOfCheck implements Meta.InstanceCheckPolicy {

    static final Meta.InstanceCheckPolicy INSTANCE = new InstanceOfCheck();

    @Override
    public boolean isSameType(Class<?> declaredType, Object thatInstance) {
      return declaredType.isInstance(thatInstance);
    }
  }

  static final class ArrayHandlingEqualsPolicy implements Meta.ObjectEqualsPolicy {

    static final Meta.ObjectEqualsPolicy INSTANCE = new ArrayHandlingEqualsPolicy();

    private static final Map<Class<?>, BiPredicate<Object, Object>> PRIMITIVE_ARRAY = new HashMap<>();
    static {
      PRIMITIVE_ARRAY.put(boolean[].class, ArrayHandlingEqualsPolicy.<boolean[]>primitiveEquals(Arrays::equals));
      PRIMITIVE_ARRAY.put(char[].class, ArrayHandlingEqualsPolicy.<char[]>primitiveEquals(Arrays::equals));
      PRIMITIVE_ARRAY.put(byte[].class, ArrayHandlingEqualsPolicy.<byte[]>primitiveEquals(Arrays::equals));
      PRIMITIVE_ARRAY.put(short[].class, ArrayHandlingEqualsPolicy.<short[]>primitiveEquals(Arrays::equals));
      PRIMITIVE_ARRAY.put(int[].class, ArrayHandlingEqualsPolicy.<int[]>primitiveEquals(Arrays::equals));
      PRIMITIVE_ARRAY.put(long[].class, ArrayHandlingEqualsPolicy.<long[]>primitiveEquals(Arrays::equals));
      PRIMITIVE_ARRAY.put(float[].class, ArrayHandlingEqualsPolicy.<float[]>primitiveEquals(Arrays::equals));
      PRIMITIVE_ARRAY.put(double[].class, ArrayHandlingEqualsPolicy.<double[]>primitiveEquals(Arrays::equals));
    }

    @Override
    public boolean areEqual(Object o1, Object o2) {
      if (o1 == o2) {
        return true;
      }
      if (o1 == null) {
        return o2 == null;
      }
      if (o2 == null) {
        return false;
      }

      if (o1.getClass().isArray()) {
        if (o2.getClass().isArray()) {
          if (o1 instanceof Object[] && o2 instanceof Object[]) {
            // object array
            Object[] a1 = (Object[]) o1;
            Object[] a2 = (Object[]) o2;
            return Arrays.equals(a1, a2);
          } else if (o1.getClass() == o2.getClass()) {
            // primitive array
            return PRIMITIVE_ARRAY.get(o1.getClass()).test(o1, o2);
          } else {
            // arrays of different types
            return false;
          }
        } else {
          return false;
        }
      }

      return Objects.equals(o1, o2);
    }

    private static <T> BiPredicate<Object, Object> primitiveEquals(BiPredicate<T, T> predicate) {
      return (Object o1, Object o2) -> {
        T a1 = (T) o1;
        T a2 = (T) o2;
        return predicate.test(a1, a2);
      };
    }
  }

  static final class ArrayHandlingHashCodePolicy implements Meta.ObjectHashCodePolicy {

    static final Meta.ObjectHashCodePolicy INSTANCE = new ArrayHandlingHashCodePolicy();

    private static final Map<Class<?>, ToIntFunction<Object>> PRIMITIVE_ARRAY = new HashMap<>();
    static {
      PRIMITIVE_ARRAY.put(boolean[].class, ArrayHandlingHashCodePolicy.<boolean[]>primitiveHashcode(Arrays::hashCode));
      PRIMITIVE_ARRAY.put(char[].class, ArrayHandlingHashCodePolicy.<char[]>primitiveHashcode(Arrays::hashCode));
      PRIMITIVE_ARRAY.put(byte[].class, ArrayHandlingHashCodePolicy.<byte[]>primitiveHashcode(Arrays::hashCode));
      PRIMITIVE_ARRAY.put(short[].class, ArrayHandlingHashCodePolicy.<short[]>primitiveHashcode(Arrays::hashCode));
      PRIMITIVE_ARRAY.put(int[].class, ArrayHandlingHashCodePolicy.<int[]>primitiveHashcode(Arrays::hashCode));
      PRIMITIVE_ARRAY.put(long[].class, ArrayHandlingHashCodePolicy.<long[]>primitiveHashcode(Arrays::hashCode));
      PRIMITIVE_ARRAY.put(float[].class, ArrayHandlingHashCodePolicy.<float[]>primitiveHashcode(Arrays::hashCode));
      PRIMITIVE_ARRAY.put(double[].class, ArrayHandlingHashCodePolicy.<double[]>primitiveHashcode(Arrays::hashCode));
    }

    @Override
    public int toHashcode(Object o) {
      if (o == null) {
        return 0;
      }
      if (o.getClass().isArray()) {
        if (o instanceof Object[]) {
          return Arrays.hashCode((Object[]) o);
        }
        return PRIMITIVE_ARRAY.get(o.getClass()).applyAsInt(o);
      }

      return o.hashCode();
    }

    private static <T> ToIntFunction<Object> primitiveHashcode(ToIntFunction<T> fn) {
      return (Object o) -> {
        T a = (T) o;
        return fn.applyAsInt(a);
      };
    }
  }
}
