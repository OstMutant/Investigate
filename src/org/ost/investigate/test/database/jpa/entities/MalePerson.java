package org.ost.investigate.test.database.jpa.entities;

import org.ost.investigate.test.database.jpa.dictionary.Gender;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(Gender.MALE)
public class MalePerson extends Person {
    protected Gender gender;
}
