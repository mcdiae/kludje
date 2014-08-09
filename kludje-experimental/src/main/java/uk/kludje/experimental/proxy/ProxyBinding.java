/*
Copyright 2014 McDowell

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

package uk.kludje.experimental.proxy;

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

import static uk.kludje.experimental.proxy.InvocationHandlers.defaultMethodHandler;
import static uk.kludje.experimental.proxy.InvocationHandlers.defaultValueHandler;

/**
 * Utility type for creating interface proxies with methods bound to functional interfaces.
 * Example:
 * <pre>
 *   AutoCloseable ac = () -&gt; System.out.println("Close!");
 *   ResultSet rc = proxy(ResultSet.class);
 *   binder(ResultSet.class, AutoCloseable.class).bind(rc::close, ac::close);
 *   rc.close();
 * </pre>
 */
public final class ProxyBinding {
  private static final InvocationHandler HANDLER = (proxy, method, args)
      -> method.isDefault()
      ? defaultMethodHandler()
      : defaultValueHandler();
  private static final ThreadLocal<Function<Object[], Object>> BINDING = new ThreadLocal<>();
  private static final ThreadLocal<Object> PROXY = new ThreadLocal<>();

  private ProxyBinding() {
  }

  /**
   * Creates a proxy instance fo a given type.
   * Methods can be bound to functional interfaces using a {@link ProxyBinding.MethodBinder}.
   *
   * @param anyInterface an interface
   * @param defaultHandler default behaviour for interface methods
   * @param <P> the interface type
   * @return a new instance
   */
  public static <P> P proxy(Class<P> anyInterface, InvocationHandler defaultHandler) {
    Objects.requireNonNull(defaultHandler, "defaultHandler");
    Assert.ensure(anyInterface.isInterface(), "anyInterface.isInterface()");

    Class<?>[] type = {anyInterface, Binding.class};
    InvocationHandler handler = new ProxyHandler(defaultHandler);
    P proxy = (P) Proxy.newProxyInstance(loader(), type, handler);
    return proxy;
  }

  /**
   * As {@link #proxy(Class, java.lang.reflect.InvocationHandler)} but with a
   * predefined no-op {@link java.lang.reflect.InvocationHandler}.
   */
  public static <P> P proxy(Class<P> anyInterface) {
    return proxy(Interface.type(anyInterface));
  }

  /**
   * @see #proxy(Class, java.lang.reflect.InvocationHandler)
   */
  public static <P> P proxy(Interface<P> anyInterface, InvocationHandler defaultHandler) {
    return proxy(anyInterface.type(), defaultHandler);
  }

  /**
   * @see #proxy(Class)
   */
  public static <P> P proxy(Interface<P> anyInterface) {
    return proxy(anyInterface, HANDLER);
  }

  /**
   * Creates a method binder for the given types.
   * {@link MethodBinder} instances returned by this method are thread safe and immutable.
   *
   * @param proxyType the proxy type
   * @param functionalInterface a functional interface type
   * @param <P> the type of the proxy
   * @param <F> the type of the interface
   * @return a new instance
   */
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
            throw Exceptions.throwChecked(e);
          } catch (InvocationTargetException e) {
            throw Exceptions.throwChecked(e);
          }
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
