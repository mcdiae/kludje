package uk.kludje;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.ToIntFunction;

final class MetaPolicy {

  private static final Map<Class<?>, BiPredicate<Object, Object>> PRIMITIVE_ARRAY_EQUALS_CHECKS = new HashMap<>();
  static {
    PRIMITIVE_ARRAY_EQUALS_CHECKS.put(boolean[].class, MetaPolicy.<boolean[]>primitiveEqualsCheck(Arrays::equals));
    PRIMITIVE_ARRAY_EQUALS_CHECKS.put(char[].class, MetaPolicy.<char[]>primitiveEqualsCheck(Arrays::equals));
    PRIMITIVE_ARRAY_EQUALS_CHECKS.put(byte[].class, MetaPolicy.<byte[]>primitiveEqualsCheck(Arrays::equals));
    PRIMITIVE_ARRAY_EQUALS_CHECKS.put(short[].class, MetaPolicy.<short[]>primitiveEqualsCheck(Arrays::equals));
    PRIMITIVE_ARRAY_EQUALS_CHECKS.put(int[].class, MetaPolicy.<int[]>primitiveEqualsCheck(Arrays::equals));
    PRIMITIVE_ARRAY_EQUALS_CHECKS.put(long[].class, MetaPolicy.<long[]>primitiveEqualsCheck(Arrays::equals));
    PRIMITIVE_ARRAY_EQUALS_CHECKS.put(float[].class, MetaPolicy.<float[]>primitiveEqualsCheck(Arrays::equals));
    PRIMITIVE_ARRAY_EQUALS_CHECKS.put(double[].class, MetaPolicy.<double[]>primitiveEqualsCheck(Arrays::equals));
  }

  private static final Map<Class<?>, ToIntFunction<Object>> PRIMITIVE_ARRAY_HASHERS = new HashMap<>();
  static {
    PRIMITIVE_ARRAY_HASHERS.put(boolean[].class, MetaPolicy.<boolean[]>primitiveHashcode(Arrays::hashCode));
    PRIMITIVE_ARRAY_HASHERS.put(char[].class, MetaPolicy.<char[]>primitiveHashcode(Arrays::hashCode));
    PRIMITIVE_ARRAY_HASHERS.put(byte[].class, MetaPolicy.<byte[]>primitiveHashcode(Arrays::hashCode));
    PRIMITIVE_ARRAY_HASHERS.put(short[].class, MetaPolicy.<short[]>primitiveHashcode(Arrays::hashCode));
    PRIMITIVE_ARRAY_HASHERS.put(int[].class, MetaPolicy.<int[]>primitiveHashcode(Arrays::hashCode));
    PRIMITIVE_ARRAY_HASHERS.put(long[].class, MetaPolicy.<long[]>primitiveHashcode(Arrays::hashCode));
    PRIMITIVE_ARRAY_HASHERS.put(float[].class, MetaPolicy.<float[]>primitiveHashcode(Arrays::hashCode));
    PRIMITIVE_ARRAY_HASHERS.put(double[].class, MetaPolicy.<double[]>primitiveHashcode(Arrays::hashCode));
  }

  private static final Map<Class<?>, Function<Object, String>> PRIMITIVE_ARRAY_STRINGIFIERS = new HashMap<>();
  static {
    PRIMITIVE_ARRAY_STRINGIFIERS.put(boolean[].class, MetaPolicy.<boolean[]>toStringFunction(Arrays::toString));
    PRIMITIVE_ARRAY_STRINGIFIERS.put(char[].class, MetaPolicy.<char[]>toStringFunction(Arrays::toString));
    PRIMITIVE_ARRAY_STRINGIFIERS.put(byte[].class, MetaPolicy.<byte[]>toStringFunction(Arrays::toString));
    PRIMITIVE_ARRAY_STRINGIFIERS.put(short[].class, MetaPolicy.<short[]>toStringFunction(Arrays::toString));
    PRIMITIVE_ARRAY_STRINGIFIERS.put(int[].class, MetaPolicy.<int[]>toStringFunction(Arrays::toString));
    PRIMITIVE_ARRAY_STRINGIFIERS.put(long[].class, MetaPolicy.<long[]>toStringFunction(Arrays::toString));
    PRIMITIVE_ARRAY_STRINGIFIERS.put(float[].class, MetaPolicy.<float[]>toStringFunction(Arrays::toString));
    PRIMITIVE_ARRAY_STRINGIFIERS.put(double[].class, MetaPolicy.<double[]>toStringFunction(Arrays::toString));
  }

  private MetaPolicy() {}

  public static boolean isSameClassInstance(Class<?> declaredType, Object thatInstance) {
    return declaredType == thatInstance.getClass();
  }

  public static boolean isInstanceOfClass(Class<?> declaredType, Object thatInstance) {
    return declaredType.isInstance(thatInstance);
  }

  public static boolean areEqualWithShallowArrayCheck(Object o1, Object o2) {
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
          return PRIMITIVE_ARRAY_EQUALS_CHECKS.get(o1.getClass())
            .test(o1, o2);
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

  private static <T> BiPredicate<Object, Object> primitiveEqualsCheck(BiPredicate<T, T> predicate) {
    return (BiPredicate<Object, Object>) predicate;
  }

  public static int toHashcodeWithShallowArrayHandling(Object o) {
    if (o == null) {
      return 0;
    }
    if (o.getClass().isArray()) {
      if (o instanceof Object[]) {
        return Arrays.hashCode((Object[]) o);
      }
      return PRIMITIVE_ARRAY_HASHERS.get(o.getClass())
        .applyAsInt(o);
    }

    return o.hashCode();
  }

  private static <T> ToIntFunction<Object> primitiveHashcode(ToIntFunction<T> fn) {
    return (ToIntFunction<Object>) fn;
  }

  public static String toStringWithShallowArrayHandling(Object o) {
    if (o == null) {
      return "null";
    }

    if (o.getClass().isArray()) {
      if (o instanceof Object[]) {
        return Arrays.toString((Object[]) o);
      }
      return PRIMITIVE_ARRAY_STRINGIFIERS.get(o.getClass())
        .apply(o);
    }

    return o.toString();
  }

  private static <T> Function<Object, String> toStringFunction(Function<T, String> fn) {
    return (Function<Object, String>) fn;
  }
}
