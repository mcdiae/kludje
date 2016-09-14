package uk.kludje.experimental.test.property.reflect;

import org.junit.Assert;
import org.junit.Test;
import uk.kludje.experimental.property.reflect.ClassFileStructure;
import uk.kludje.experimental.test.property.reflect.target.ObjectGetter;

import java.io.IOException;
import java.net.URL;

public class ClassFileStructureTest {

  @Test
  public void testSelfParsing() throws IOException {
    ClassFileStructure itself = ClassFileStructure.parse(toUrl(ClassFileStructure.class));
    Assert.assertNotNull(itself);
  }

  @Test
  public void testParseObjectGetter() throws IOException {
    ClassFileStructure getter = ClassFileStructure.parse(toUrl(ObjectGetter.class));
    Assert.assertNotNull(getter);
  }

  private URL toUrl(Class<?> cls) {
    String name = cls.getName().replace('.', '/') + ".class";
    URL url = cls.getClassLoader().getResource(name);
    Assert.assertNotNull(url);
    return url;
  }
}
