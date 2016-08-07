/*
Copyright 2016 McDowell

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
