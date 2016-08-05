package uk.kludje.test;

import static java.lang.Float.NaN;
import static org.junit.Assert.*;
import org.junit.Test;
import uk.kludje.MetaConfig;

import java.util.Arrays;
import java.util.Objects;

public class MetaConfigTest {

  @Test
  public void testDefaultMetaConfigAlwaysSameObject() {
    // invoke
    MetaConfig default1 = MetaConfig.defaultConfig();
    MetaConfig default2 = MetaConfig.defaultConfig();
    // verify
    assertNotNull(default1);
    assertTrue(default1 == default2);
  }

  @Test
  public void testSetEmptyNamePolicy() {
    // setup
    MetaConfig.EmptyNamePolicy policy = property -> "burger";
    // invoke
    MetaConfig config = MetaConfig.defaultConfig()
      .withEmptyNamePolicy(policy);
    // verify
    assertTrue(policy == config.getEmptyNamePolicy());
  }

  @Test(expected = Error.class)
  public void testInstanceCheckNullHandling() {
    MetaConfig.defaultConfig()
      .withInstanceCheck(null);
  }

  @Test(expected = Error.class)
  public void testObjectEqualsNullHandling() {
    MetaConfig.defaultConfig()
      .withObjectEqualsChecks(null, MetaConfig.defaultConfig().getObjectHashCodePolicy());
  }

  @Test(expected = Error.class)
  public void testObjectHashCodeNullHandling() {
    MetaConfig.defaultConfig()
      .withObjectEqualsChecks(MetaConfig.defaultConfig().getObjectEqualsPolicy(), null);
  }

  @Test(expected = Error.class)
  public void testObjectToStringNullHandling() {
    MetaConfig.defaultConfig()
      .withObjectToString(null);
  }

  @Test(expected = Error.class)
  public void testEmptyNamePolicyNullHandling() {
    MetaConfig.defaultConfig()
      .withEmptyNamePolicy(null);
  }

  @Test
  public void testShallowArrayEqualityHandling() {
    // setup
    MetaConfig config = MetaConfig.defaultConfig()
      .withShallowArraySupport();
    MetaConfig.ObjectEqualsPolicy equals = config.getObjectEqualsPolicy();
    // verify basic equality
    assertTrue(equals.areEqual("foo", "foo"));
    assertTrue(equals.areEqual(null, null));
    assertFalse(equals.areEqual(null, "foo"));
    assertFalse(equals.areEqual("foo", null));
    assertFalse(equals.areEqual(null, new Object[0]));
    assertFalse(equals.areEqual(new int[0], new Object[0]));
    assertFalse(equals.areEqual(new Object[0], new int[0]));
    assertFalse(equals.areEqual(new int[0], null));
    assertFalse(equals.areEqual(new int[0], new byte[0]));
    assertFalse(equals.areEqual(new int[0], new int[1]));
    assertFalse(equals.areEqual(new int[0], new Object()));
    assertFalse(equals.areEqual(new Object(), new Object()));
    // verify primitive array equality
    assertTrue(equals.areEqual(new boolean[]{true}, new boolean[]{true}));
    assertFalse(equals.areEqual(new boolean[]{true}, new boolean[]{false}));
    assertTrue(equals.areEqual(new char[]{'a'}, new char[]{'a'}));
    assertFalse(equals.areEqual(new char[]{'a'}, new char[]{'b'}));
    assertTrue(equals.areEqual(new byte[]{0}, new byte[]{0}));
    assertFalse(equals.areEqual(new byte[]{0}, new byte[]{1}));
    assertTrue(equals.areEqual(new short[]{0}, new short[]{0}));
    assertFalse(equals.areEqual(new short[]{0}, new short[]{1}));
    assertTrue(equals.areEqual(new int[]{0}, new int[]{0}));
    assertFalse(equals.areEqual(new int[]{0}, new int[]{1, 1}));
    assertTrue(equals.areEqual(new long[]{0}, new long[]{0}));
    assertFalse(equals.areEqual(new long[]{0}, new long[]{1, 1}));
    assertTrue(equals.areEqual(new float[]{0.0F}, new float[]{0.0F}));
    assertFalse(equals.areEqual(new float[]{0.0F}, new float[]{NaN, 1}));
    assertTrue(equals.areEqual(new double[]{0.0}, new double[]{0.0}));
    assertFalse(equals.areEqual(new double[]{0.0}, new double[]{NaN, 1}));
    // verify object array equality
    assertTrue(equals.areEqual(new Object[]{true}, new Object[]{true}));
    assertFalse(equals.areEqual(new Object[]{true}, new Object[]{false}));
  }

  @Test
  public void testShallowArrayHashHandling() {
    // setup
    MetaConfig config = MetaConfig.defaultConfig()
      .withShallowArraySupport();
    MetaConfig.ObjectHashCodePolicy hashes = config.getObjectHashCodePolicy();
    //MetaConfig.ObjectToStringPolicy strings = config.getObjectToStringPolicy();
    // verify basic hashCodes
    assertEquals("foo".hashCode(), hashes.toHashcode("foo"));
    assertEquals(0, hashes.toHashcode(null));
    // verify primitive array hashCodes
    assertEquals(Arrays.hashCode(new boolean[]{true}), hashes.toHashcode(new boolean[]{true}));
    assertEquals(Arrays.hashCode(new char[]{'Z'}), hashes.toHashcode(new char[]{'Z'}));
    assertEquals(Arrays.hashCode(new byte[]{13}), hashes.toHashcode(new byte[]{13}));
    assertEquals(Arrays.hashCode(new short[]{13}), hashes.toHashcode(new short[]{13}));
    assertEquals(Arrays.hashCode(new int[]{13}), hashes.toHashcode(new int[]{13}));
    assertEquals(Arrays.hashCode(new long[]{13}), hashes.toHashcode(new long[]{13}));
    assertEquals(Arrays.hashCode(new float[]{13}), hashes.toHashcode(new float[]{13}));
    assertEquals(Arrays.hashCode(new double[]{13}), hashes.toHashcode(new double[]{13}));
    // verify object array hashCodes
    assertEquals(Arrays.hashCode(new Object[]{13}), hashes.toHashcode(new Object[]{13}));
  }

  @Test
  public void testShallowArrayStringHandling() {
    // setup
    MetaConfig config = MetaConfig.defaultConfig()
      .withShallowArraySupport();
    MetaConfig.ObjectToStringPolicy strings = config.getObjectToStringPolicy();
    // verify basic hashCodes
    assertEquals("foo", strings.toString("foo"));
    assertEquals(Objects.toString(null), strings.toString(null));
    // verify primitive array hashCodes
    assertEquals(Arrays.toString(new boolean[]{true}), strings.toString(new boolean[]{true}));
    assertEquals(Arrays.toString(new char[]{'\u20AC'}), strings.toString(new char[]{'\u20AC'}));
    assertEquals(Arrays.toString(new byte[]{12}), strings.toString(new byte[]{12}));
    assertEquals(Arrays.toString(new short[]{12}), strings.toString(new short[]{12}));
    assertEquals(Arrays.toString(new int[]{12}), strings.toString(new int[]{12}));
    assertEquals(Arrays.toString(new long[]{12}), strings.toString(new long[]{12}));
    assertEquals(Arrays.toString(new float[]{12}), strings.toString(new float[]{12}));
    assertEquals(Arrays.toString(new double[]{12}), strings.toString(new double[]{12}));
    // verify object array hashCodes
    assertEquals(Arrays.toString(new Double[]{12.0}), strings.toString(new Double[]{12.0}));
  }

  @Test
  public void testInstanceofPolicy() {
    class X {}
    class Y extends X {}
    // setup
    MetaConfig.InstanceCheckPolicy policy = MetaConfig.defaultConfig()
      .withInstanceofEqualsTypeCheck()
      .getInstanceCheckPolicy();
    // verify
    assertTrue(policy.isSameType(X.class, new Y()));
    assertFalse(policy.isSameType(X.class, new Object()));
  }

  @Test
  public void testDefaultInstanceCheckPolicy() {
    class X {}
    class Y extends X {}
    // setup
    MetaConfig.InstanceCheckPolicy policy = MetaConfig.defaultConfig()
      .getInstanceCheckPolicy();
    // verify
    assertFalse(policy.isSameType(X.class, new Y()));
    assertFalse(policy.isSameType(X.class, new Object()));
  }
}
