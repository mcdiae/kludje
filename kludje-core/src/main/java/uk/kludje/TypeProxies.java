package uk.kludje;

final class TypeProxies<T> {
  private final Class<?>[] type;

  private TypeProxies(Class<?>[] type) {
    this.type = type;
  }

  public ProxyFactory<T> instance(T decorated) {
    return null;
  }

  public ProxyFactory<T> unsupported() {
    return null;
  }

  public ProxyFactory<T> defaults(T decorated) {
    return null;
  }

  public static <T> TypeProxies<T> proxyFactory(Class<T> type) {
    Class<?>[] arr = { type };
    return new TypeProxies<>(arr);
  }

  public static final class ProxyFactory<T> {

  }
}
