/*
Copyright 2016 McDowell

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

package uk.kludje;

import uk.kludje.property.Getter;

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

  private final InstanceCheckPolicy instanceCheckPolicy;
  private final ObjectEqualsPolicy objectEqualsPolicy;
  private final ObjectHashCodePolicy objectHashCodePolicy;
  private final ObjectToStringPolicy objectToStringPolicy;

  private MetaConfig(MetaConfigBuilder builder) {
    instanceCheckPolicy = builder.instanceCheckPolicy;
    objectEqualsPolicy = builder.objectEqualsPolicy;
    objectHashCodePolicy = builder.objectHashCodePolicy;
    objectToStringPolicy = builder.objectToStringPolicy;
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
   * Called at the start of {@link Meta#equals(Object, Object)} to ensure that the 2nd argument is the same type
   * as the first before it is cast to that type.
   *
   * The default policy returns true if {@code this.getClass() == that.getClass()}.
   *
   * @return the policy
   * @see InstanceCheckPolicy
   * @see #withInstanceofEqualsTypeCheck()
   */
  public InstanceCheckPolicy getInstanceCheckPolicy() {
    return instanceCheckPolicy;
  }

  /**
   * Used in {@link Meta#equals(Object, Object)} for any return value from {@link Getter} to check whether
   * values are equal.
   *
   * The default policy uses {@link Objects#equals(Object, Object)}.
   *
   * @return the policy
   * @see #withShallowArraySupport()
   */
  public ObjectEqualsPolicy getObjectEqualsPolicy() {
    return objectEqualsPolicy;
  }

  /**
   * Used in {@link Meta#hashCode(Object)} for any return value from {@link Getter}.
   *
   * The default policy uses {@link Objects#hashCode(Object)}.
   *
   * @return the policy
   * @see #withShallowArraySupport()
   */
  public ObjectHashCodePolicy getObjectHashCodePolicy() {
    return objectHashCodePolicy;
  }

  /**
   * Used in {@link Meta#toString(Object)} for any return value from {@link Getter}.
   *
   * The default policy uses {@link Objects#toString(Object)}.
   *
   * @return the policy
   * @see #withShallowArraySupport()
   */
  public ObjectToStringPolicy getObjectToStringPolicy() {
    return objectToStringPolicy;
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
    return withInstanceCheck(MetaPolicy::isInstanceOfClass);
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
    return withObjectEqualsChecks(MetaPolicy::areEqualWithShallowArrayCheck, MetaPolicy::toHashcodeWithShallowArrayHandling)
      .withObjectToString(MetaPolicy::toStringWithShallowArrayHandling);
  }

  /**
   * Allows consumers to set special handling for how {@link Meta#equals(Object, Object)} determines whether
   * the 2nd argument is the same type as the first.
   *
   * @param policy a non-null policy
   * @return the new config
   * @see #withInstanceofEqualsTypeCheck()
   */
  public MetaConfig withInstanceCheck(InstanceCheckPolicy policy) {
    Ensure.that(policy != null, "policy != null");

    return new MetaConfigBuilder(this)
      .setInstanceCheckPolicy(policy)
      .build();
  }

  /**
   * Allows consumers to set special handling for {@link Getter#get(Object)} responses for
   * {@link Meta#equals(Object, Object)}, {@link Meta#hashCode(Object).
   *
   * @param equalsPolicy a non-null equals policy
   * @param hashCodePolicy a non-null hash policy
   * @return the new config
   * @see #withShallowArraySupport()
   */
  public MetaConfig withObjectEqualsChecks(ObjectEqualsPolicy equalsPolicy, ObjectHashCodePolicy hashCodePolicy) {
    Ensure.that(equalsPolicy != null, "equalsPolicy != null");
    Ensure.that(hashCodePolicy != null, "hashCodePolicy != null");

    return new MetaConfigBuilder(this)
      .setObjectEqualsPolicy(equalsPolicy)
      .setObjectHashCodePolicy(hashCodePolicy)
      .build();
  }

  /**
   * Allows consumers to set special handling for {@link Getter#get(Object)} responses for
   * {@link Meta#toString(Object)}.
   *
   * @param stringPolicy a non-null to string policy
   * @return the new config
   * @see #withShallowArraySupport()
   */
  public MetaConfig withObjectToString(ObjectToStringPolicy stringPolicy) {
    Ensure.that(stringPolicy != null, "stringPolicy != null");

    return new MetaConfigBuilder(this)
      .setObjectToStringPolicy(stringPolicy)
      .build();
  }

  private static final class MetaConfigBuilder {

    private InstanceCheckPolicy instanceCheckPolicy;
    private ObjectEqualsPolicy objectEqualsPolicy;
    private ObjectHashCodePolicy objectHashCodePolicy;
    private ObjectToStringPolicy objectToStringPolicy;

    private MetaConfigBuilder() {
      instanceCheckPolicy = MetaPolicy::isSameClassInstance;
      objectEqualsPolicy = Objects::equals;
      objectHashCodePolicy = Objects::hashCode;
      objectToStringPolicy = Objects::toString;
    }

    private MetaConfigBuilder(MetaConfig defaultInstance) {
      instanceCheckPolicy = defaultInstance.instanceCheckPolicy;
      objectEqualsPolicy = defaultInstance.objectEqualsPolicy;
      objectHashCodePolicy = defaultInstance.objectHashCodePolicy;
      objectToStringPolicy = defaultInstance.objectToStringPolicy;
    }

    MetaConfig build() {
      return new MetaConfig(this);
    }

    MetaConfigBuilder setInstanceCheckPolicy(InstanceCheckPolicy instanceCheckPolicy) {
      this.instanceCheckPolicy = instanceCheckPolicy;
      return this;
    }

    MetaConfigBuilder setObjectHashCodePolicy(ObjectHashCodePolicy objectHashCodePolicy) {
      this.objectHashCodePolicy = objectHashCodePolicy;
      return this;
    }

    MetaConfigBuilder setObjectToStringPolicy(ObjectToStringPolicy objectToStringPolicy) {
      this.objectToStringPolicy = objectToStringPolicy;
      return this;
    }

    MetaConfigBuilder setObjectEqualsPolicy(ObjectEqualsPolicy objectEqualsPolicy) {
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
}
