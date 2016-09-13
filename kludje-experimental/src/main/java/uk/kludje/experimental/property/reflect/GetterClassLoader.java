package uk.kludje.experimental.property.reflect;

public class GetterClassLoader extends ClassLoader {

  public GetterClassLoader(ClassLoader parent) {
    super(parent);
  }

  public Class<?> loadGeneratedClass(String name, byte[] byteCode) {
    return this.defineClass(name, byteCode, 0, byteCode.length);
  }
}
