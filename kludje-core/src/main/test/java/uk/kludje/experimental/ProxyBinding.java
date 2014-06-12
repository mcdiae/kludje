package uk.kludje.experimental;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public final class ProxyBinding {
  private ProxyBinding () {}

  public static <P> P proxy(Interface<P> anyInterface) {
    return null;
  }

  public static <P> P proxy(Class<P> anyInterface) {
    return null;
  }

  public static <P> P proxy(Interface<P> anyInterface, InvocationHandler defaultHandler) {
    return null;
  }

  public static <P> P proxy(Class<P> anyInterface, InvocationHandler defaultHandler) {
    return null;
  }

  public static <P, F> MethodBinder<P, F> binder(Interface<P> proxyType, Interface<F> functionalInterface) {
    return null;
  }

  public static interface MethodBinder<P, F> {
    public P bind(F proxyRef, F action);
  }

  private static interface Binding {
    void set$action(Object o);
  }

  private static class ProxyHandler implements InvocationHandler {
    private static final Method BINDING = findBinding();
    private Object binding;
    private final Map<Method, Object> bindings = new HashMap<>();

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
      if(binding == null) {
        Object action = bindings.get(method);
      }

      return null;
    }
  }

  private static Method findBinding() {
    try {
      return Binding.class.getMethod("set$action", Object.class);
    } catch (NoSuchMethodException e) {
      throw new AssertionError(e);
    }
  }
}
