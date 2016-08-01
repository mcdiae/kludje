package uk.kludje;

import uk.kludje.property.Getter;
import uk.kludje.property.TypedProperty;

import java.util.Objects;

/**
 * Allows more flexible configuration of {@link Meta} instances.
 * Use the default configuration to create new configurations.
 * Instances must be immutable and thread-safe.
 *
 * @see Meta#configure(MetaConfig)
 * @see MetaConfig#defaultConfig()
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

  /**
   * The default configuration for {@link Meta} instances.
   * It is not possible to alter the default configuration.
   * Callers must generate new configurations using methods like
   * {@link #withShallowArraySupport()} and then use {@link Meta#configure(MetaConfig)}
   * to create a new instance with the given configuration.
   *
   * @return the default configuration
   */
  public static MetaConfig defaultConfig() {
    return DEFAULT_POLICY;
  }
  
  /**
   * Alters the config to use {@code thisType.isInstance(thatInstance)} instead of
   * the default {@code thisType == thatInstance.getClass()}.
   *
   * Use this method if you need subtypes to be equal to the parent type {@code T}.
   *
   * @return a new config
   * @see MetaConfig.InstanceCheckPolicy
   */
  public MetaConfig withInstanceofEqualsTypeCheck() {
    return new MetaConfigBuilder(this)
      .setInstanceCheckPolicy(MetaPolicy::isInstanceOfClass)
      .build();
  }

  /**
   * Alters the configuration to have basic array support.
   * Where a {@link Getter} returns an array the configuration will support shallow equality, hash code and to string
   * inspection to determine equivalence.
   * Refer to the following methods for base implementation details:
   * {@link java.util.Arrays#equals(boolean[], boolean[])};
   * {@link java.util.Arrays#hashCode(boolean[])};
   * {@link java.util.Arrays#toString(boolean[])}.
   *
   * @return a new configuration with array handling support
   * @see Meta#equals(Object, Object)
   * @see Meta#hashCode(Object)
   * @see Meta#toString(Object)
   */
  public MetaConfig withShallowArraySupport() {
    return new MetaConfigBuilder(this)
      .setObjectEqualsPolicy(MetaPolicy::areEqualWithShallowArrayCheck)
      .setObjectHashCodePolicy(MetaPolicy::toHashcodeWithShallowArrayHandling)
      .setObjectToStringPolicy(MetaPolicy::toStringWithShallowArrayHandling)
      .build();
  }

  private static final class MetaConfigBuilder {

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

  /**
   * Used at the start of {@link Meta#equals(Object, Object)} to check type equivalence.
   * Instances must be immutable and thread-safe.
   *
   * @see MetaConfig#withInstanceofEqualsTypeCheck()
   */
  @FunctionalInterface
  public interface InstanceCheckPolicy {

    /**
     * @param thisType the meta type
     * @param thatInstance the instance to check
     * @return true if thatInstance is the "same" type as thisType; false otherwise
     */
    boolean isSameType(Class<?> thisType, Object thatInstance);
  }

  /**
   * Allows configuration of how objects are considered equal.
   * Used in {@link Meta#equals(Object, Object)} when {@link Getter#type()} == {@link uk.kludje.property.PropertyType#OBJECT}.
   * Instances must be immutable and thread-safe.
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
   * Allows configuration of how an object's hash code is generated.
   * Used in {@link Meta#hashCode(Object)} when {@link Getter#type()} == {@link uk.kludje.property.PropertyType#OBJECT}.
   * Instances must be immutable and thread-safe.
   */
  @FunctionalInterface
  public interface ObjectHashCodePolicy {

    /**
     * @param o the object
     * @return the hash code
     */
    int toHashcode(Object o);
  }

  /**
   * Allows configuration of how objects are converted to strings.
   * Used in {@link Meta#toString(Object)} when {@link Getter#type()} == {@link uk.kludje.property.PropertyType#OBJECT}.
   * Instances must be immutable and thread-safe.
   */
  @FunctionalInterface
  public interface ObjectToStringPolicy {
    String toString(Object o);
  }

  /**
   * Allows alternative interpretation of a typed property that has not been formally named.
   * Instances must be immutable and thread-safe.
   */
  @FunctionalInterface
  public interface EmptyNamePolicy {
    /**
     * @param property the getter
     * @return a String; must not be null
     */
    String toName(TypedProperty property);
  }
}
