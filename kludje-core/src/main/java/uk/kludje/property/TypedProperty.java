package uk.kludje.property;

/**
 * 3rd parties need not normally implement this type.
 * All instances of this type returned by the {@link PropertyGetterList} type are instances of a getter type.
 *
 * @see Getter
 * @see BooleanGetter
 * @see CharGetter
 * @see ByteGetter
 * @see ShortGetter
 * @see IntGetter
 * @see LongGetter
 * @see FloatGetter
 * @see DoubleGetter
 */
public interface TypedProperty {
  PropertyType type();
}
