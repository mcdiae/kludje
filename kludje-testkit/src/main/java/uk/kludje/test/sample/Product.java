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

import uk.kludje.Meta;

public class Product {
  private static final Meta<Product> META = Meta.meta(Product.class)
      .longs($ -> $.id)
      .objects($ -> $.description)
      .ints($ -> $.inventory);

  private final long id;
  private final String description;
  private final int inventory;

  public Product(long id, String description, int inventory) {
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
  public boolean equals(Object obj) {
    return META.equals(this, obj);
  }

  @Override
  public int hashCode() {
    return META.hashCode(this);
  }

  @Override
  public String toString() {
    return META.toString(this);
  }
}
