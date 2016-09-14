package uk.kludje.experimental.test.property.reflect.target;

public class ObjectPojo {

  private final Object value;

  public ObjectPojo(Object value) {
    this.value = value;
  }
  
  public Object value() {
    return value;
  }
}
