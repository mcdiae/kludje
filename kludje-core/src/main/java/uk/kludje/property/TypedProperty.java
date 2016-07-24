package uk.kludje.property;

import uk.kludje.Meta;

/**
 * All instances of this type returned by the {@link Meta} type are instances of a getter type.
 *
 * @see Meta.Getter
 * @see Meta.BooleanGetter
 * @see Meta.CharGetter
 * @see Meta.ByteGetter
 * @see Meta.ShortGetter
 * @see Meta.IntGetter
 * @see Meta.LongGetter
 * @see Meta.FloatGetter
 * @see Meta.DoubleGetter
 */
public interface TypedProperty {
  PropertyType type();
}
