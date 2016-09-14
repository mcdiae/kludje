package uk.kludje.experimental.property.reflect;

import java.util.logging.Level;
import java.util.logging.Logger;

class Log {

  private final Logger log;

  private Log(Logger log) {
    this.log = log;
  }

  public void debug(String fmt, Object arg) {
    if (log.isLoggable(Level.FINE)) {
      debugIt(fmt, arg);
    }
  }

  public void debug(String fmt, Object arg1, Object arg2) {
    if (log.isLoggable(Level.FINE)) {
      debugIt(fmt, arg1, arg2);
    }
  }

  private void debugIt(String fmt, Object...args) {
    String result = String.format(fmt, args);
    log.fine(result);
  }

  public static Log logger(Class<?> cls) {
    return new Log(Logger.getLogger(cls.getName()));
  }
}
