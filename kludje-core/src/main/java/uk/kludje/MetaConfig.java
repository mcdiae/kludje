package uk.kludje;

import uk.kludje.property.TypedProperty;

import java.util.Objects;

/**
 * Created by user on 29/07/16.
 */
public final class MetaConfig {

  private static final MetaConfig DEFAULT_POLICY = new MetaConfigBuilder().build();

  final InstanceCheckPolicy instanceCheckPolicy;
  final ObjectEqualsPolicy objectEqualsPolicy;
  final ObjectHashCodePolicy objectHashCodePolicy;
  final ObjectToStringPolicy objectToStringPolicy;
  final EmptyNamePolicy emptyNamePolicy;

  private MetaConfig(MetaConfigBuilder builder) {
    instanceCheckPolicy = builder.instanceCheckPolicy;
    objectEqualsPolicy = builder.objectEqualsPolicy;
    objectHashCodePolicy = builder.objectHashCodePolicy;
    objectToStringPolicy = builder.objectToStringPolicy;
    emptyNamePolicy = builder.emptyNamePolicy;
  }

  public static MetaConfig defaultConfig() {
    return DEFAULT_POLICY;
  }

  public MetaConfig withInstanceofEqualsTypeCheck() {
    return new MetaConfigBuilder()
      .setInstanceCheckPolicy(MetaPolicy::isInstanceOfClass)
      .build();
  }

  public MetaConfig withShallowArraySupport() {
    return new MetaConfigBuilder()
      .setObjectEqualsPolicy(MetaPolicy::areEqualWithShallowArrayCheck)
      .setObjectHashCodePolicy(MetaPolicy::toHashcodeWithShallowArrayHandling)
      .setObjectToStringPolicy(MetaPolicy::toStringWithShallowArrayHandling)
      .build();
  }

  public static final class MetaConfigBuilder {

    private InstanceCheckPolicy instanceCheckPolicy;
    private ObjectEqualsPolicy objectEqualsPolicy;
    private ObjectHashCodePolicy objectHashCodePolicy;
    private ObjectToStringPolicy objectToStringPolicy;
    private EmptyNamePolicy emptyNamePolicy;

    private MetaConfigBuilder() {
      instanceCheckPolicy = MetaPolicy::isSameClassInstance;
      objectEqualsPolicy = Objects::equals;
      objectHashCodePolicy = Objects::hashCode;
      objectToStringPolicy = Objects::toString;
      emptyNamePolicy = o -> "";
    }

    private MetaConfigBuilder(MetaConfig defaultInstance) {
      instanceCheckPolicy = defaultInstance.instanceCheckPolicy;
      objectEqualsPolicy = defaultInstance.objectEqualsPolicy;
      objectHashCodePolicy = defaultInstance.objectHashCodePolicy;
      objectToStringPolicy = defaultInstance.objectToStringPolicy;
      emptyNamePolicy = defaultInstance.emptyNamePolicy;
    }

    public MetaConfig build() {
      return new MetaConfig(this);
    }

    public MetaConfigBuilder setInstanceCheckPolicy(InstanceCheckPolicy instanceCheckPolicy) {
      this.instanceCheckPolicy = instanceCheckPolicy;
      return this;
    }

    public MetaConfigBuilder setObjectHashCodePolicy(ObjectHashCodePolicy objectHashCodePolicy) {
      this.objectHashCodePolicy = objectHashCodePolicy;
      return this;
    }

    public MetaConfigBuilder setObjectToStringPolicy(ObjectToStringPolicy objectToStringPolicy) {
      this.objectToStringPolicy = objectToStringPolicy;
      return this;
    }

    public MetaConfigBuilder setEmptyNamePolicy(EmptyNamePolicy emptyNamePolicy) {
      this.emptyNamePolicy = emptyNamePolicy;
      return this;
    }

    public MetaConfigBuilder setObjectEqualsPolicy(ObjectEqualsPolicy objectEqualsPolicy) {
      this.objectEqualsPolicy = objectEqualsPolicy;
      return this;
    }
  }

  @FunctionalInterface
  public interface InstanceCheckPolicy {

    /**
     * @param declaredType the meta type
     * @param thatInstance the instance to check
     * @return true if thatInstance is the "same" type as declaredType; false otherwise
     */
    boolean isSameType(Class<?> declaredType, Object thatInstance);
  }

  /**
   * Allows callers to configure how objects are considered equal.
   */
  @FunctionalInterface
  public interface ObjectEqualsPolicy {

    /**
     * @param o1 an object
     * @param o2 another object
     * @return true if the objects are considered equal
     */
    boolean areEqual(Object o1, Object o2);
  }

  /**
   * Allows callers to configure how objects are translated to hash codes.
   */
  @FunctionalInterface
  public interface ObjectHashCodePolicy {

    /**
     * @param o the object
     * @return the hash code
     */
    int toHashcode(Object o);
  }

  @FunctionalInterface
  public interface ObjectToStringPolicy {
    String toString(Object o);
  }

  @FunctionalInterface
  public interface EmptyNamePolicy {
    String toName(TypedProperty property);
  }
}
