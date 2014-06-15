package uk.kludje.experimental;

import uk.kludje.Exceptions;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

public final class ProxyBinding {
  private static final InvocationHandler HANDLER = (proxy, method, args)
      -> method.isDefault()
      ? InvocationHandlers.defaultMethodHandler()
      : InvocationHandlers.defaultValueHandler();
  private static final ThreadLocal<Function<Object[], Object>> BINDING = new ThreadLocal<>();
  private static final ThreadLocal<Object> PROXY = new ThreadLocal<>();

  private ProxyBinding() {
  }

  public static <P> P proxy(Interface<P> anyInterface, InvocationHandler defaultHandler) {
    Class<?>[] type = {anyInterface.type(), Binding.class};
    P proxy = (P) Proxy.newProxyInstance(loader(), type, new ProxyHandler(defaultHandler));
    return proxy;
  }

  public static <P> P proxy(Interface<P> anyInterface) {
    return proxy(anyInterface, HANDLER);
  }

  public static <P> P proxy(Class<P> anyInterface) {
    return proxy(Interface.type(anyInterface));
  }

  public static <P> P proxy(Class<P> anyInterface, InvocationHandler defaultHandler) {
    return proxy(Interface.type(anyInterface), defaultHandler);
  }

  public static <P, F> MethodBinder<P, F> binder(Interface<P> proxyType, Interface<F> functionalInterface) {
    Objects.requireNonNull(proxyType, "proxyType");
    Objects.requireNonNull(functionalInterface, "functionalInterface");
    Assert.ensure(functionalInterface.abstractMethods().size() == 1, "functionalInterface.abstractMethods().size() == 1");

    return (proxyRef, action) -> {
      Objects.requireNonNull(proxyRef, "proxyRef");
      Objects.requireNonNull(action, "action");

      try {

        Method sam = functionalInterface.abstractMethods()
            .get(0);
        BINDING.set((args) -> {
          try {
            return sam.invoke(action, args);
          } catch (IllegalAccessException e) {
            Exceptions.throwChecked(e);
          } catch (InvocationTargetException e) {
            Exceptions.throwChecked(e);
          }
          throw new AssertionError();
        });

        try {
          sam.invoke(proxyRef, Methods.defaultArgs(sam));
        } catch (IllegalAccessException e) {
          Exceptions.throwChecked(e);
        } catch (InvocationTargetException e) {
          Exceptions.throwChecked(e);
        }

        P proxy = (P) PROXY.get();

        if (proxy == null) {
          throw new IllegalArgumentException("proxy ref does not appear to be a method reference of a proxy produced by "
              + ProxyBinding.class
              + "; validate "
              + proxyRef);
        }

        return proxy;
      } finally {
        PROXY.remove();
        BINDING.remove();
      }
    };
  }

  public static <P, F> MethodBinder<P, F> binder(Class<P> proxyType, Interface<F> functionalInterface) {
    return binder(Interface.type(proxyType), functionalInterface);
  }

  public static <P, F> MethodBinder<P, F> binder(Interface<P> proxyType, Class<F> functionalInterface) {
    return binder(proxyType, Interface.type(functionalInterface));
  }

  public static <P, F> MethodBinder<P, F> binder(Class<P> proxyType, Class<F> functionalInterface) {
    Interface<P> pt = Interface.type(proxyType);
    Interface<F> fi = Interface.type(functionalInterface);
    return binder(pt, fi);
  }

  private static Method findBinding() {
    try {
      return Binding.class.getMethod("set$action", Object.class);
    } catch (NoSuchMethodException e) {
      throw new AssertionError(e);
    }
  }

  private static ClassLoader loader() {
    ClassLoader loader = Thread.currentThread().getContextClassLoader();
    if (loader == null) {
      loader = ProxyBinding.class.getClassLoader();
    }
    if (loader == null) {
      loader = ClassLoader.getSystemClassLoader();
    }
    return loader;
  }

  public static interface MethodBinder<P, F> {
    public P bind(F proxyRef, F action);
  }
  private static interface Binding {
    void set$action(Supplier<Object> o);
  }

  private static class ProxyHandler implements InvocationHandler {
    private final Map<Method, Function<Object[], Object>> bindings = new HashMap<>();
    private final InvocationHandler decorated;

    public ProxyHandler(InvocationHandler decorated) {
      this.decorated = Objects.requireNonNull(decorated, "InvocationHandler");
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
      Function<Object[], Object> binding = BINDING.get();

      if (binding == null) {
        Function<Object[], Object> action = bindings.get(method);
        if (action != null) {
          return action.apply(args);
        }
      } else {
        bindings.put(method, binding);
        PROXY.set(proxy);
        return method.getDefaultValue();
      }

      return decorated.invoke(proxy, method, args);
    }
  }
}
