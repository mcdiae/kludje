package uk.kludje.test;

import uk.kludje.Meta;
import java.time.LocalDate;
import static uk.kludje.Meta.meta;

public class PersonPojo {
  private static final Meta<PersonPojo> META = meta(PersonPojo.class)
      .longs($ -> $.id).objects($ -> $.name, $ -> $.dateOfBirth);

  private final long id;
  private final String name;
  private final LocalDate dateOfBirth;

  public PersonPojo(long id, String name, LocalDate dateOfBirth) {
    this.id = id;
    this.name = name;
    this.dateOfBirth = dateOfBirth;
  }

  public String getName() {
    return name;
  }

  public LocalDate getDateOfBirth() {
    return dateOfBirth;
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
