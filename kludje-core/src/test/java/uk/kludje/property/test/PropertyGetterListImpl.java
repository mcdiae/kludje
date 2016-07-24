package uk.kludje.property.test;

import org.junit.Ignore;
import uk.kludje.property.PropertyGetterList;
import uk.kludje.property.TypedProperty;

/** Simple inheritance test */
@Ignore
public class PropertyGetterListImpl<T> extends PropertyGetterList<T, PropertyGetterListImpl<T>> {

  private final TypedProperty[] properties;
  private final String[] names;

  private PropertyGetterListImpl(String[] names, TypedProperty[] properties) {
    this.names = names;
    this.properties = properties;
  }

  @Override
  protected PropertyGetterListImpl<T> newInstance(PropertyGetterListImpl<T> old, String name, TypedProperty getter) {
    return null;
  }

  @Override
  public int size() {
    return properties.length;
  }

  @Override
  public String nameAt(int index) {
    return names[index];
  }

  @Override
  public TypedProperty propertyAt(int index) {
    return properties[index];
  }
}
