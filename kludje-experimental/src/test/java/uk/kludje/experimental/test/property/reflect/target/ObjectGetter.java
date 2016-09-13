package uk.kludje.experimental.test.property.reflect.target;

import uk.kludje.property.Getter;

public class ObjectGetter implements Getter<ObjectPojo> {

  @Override
  public Object get(ObjectPojo objectPojo) {
    return objectPojo.value();
  }
}
