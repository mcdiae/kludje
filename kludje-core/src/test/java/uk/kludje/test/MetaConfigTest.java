package uk.kludje.test;

import static org.junit.Assert.*;
import org.junit.Test;
import uk.kludje.MetaConfig;

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
}
