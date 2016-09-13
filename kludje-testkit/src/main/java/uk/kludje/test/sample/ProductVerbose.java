/*
 * Copyright 2014 McDowell
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.kludje.test.sample;

public class ProductVerbose {

  private final long id;
  private final String description;
  private final int inventory;

  public ProductVerbose(long id, String description, int inventory) {
    this.id = id;
    this.description = description;
    this.inventory = inventory;
  }

  public long getId() {
    return id;
  }

  public String getDescription() {
    return description;
  }

  public int getInventory() {
    return inventory;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    ProductVerbose that = (ProductVerbose) o;

    if (id != that.id) return false;
    if (inventory != that.inventory) return false;
    return description != null ? description.equals(that.description) : that.description == null;

  }

  @Override
  public int hashCode() {
    int result = (int) (id ^ (id >>> 32));
    result = 31 * result + (description != null ? description.hashCode() : 0);
    result = 31 * result + inventory;
    return result;
  }

  @Override
  public String toString() {
    return "ProductVerbose {" +
      id
      + ", "
      + description
      + ", "
      + inventory
      + '}';
  }
}
