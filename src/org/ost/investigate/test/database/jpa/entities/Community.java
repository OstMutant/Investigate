package org.ost.investigate.test.database.jpa.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "community")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Community {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private int id;

    private String name = null;

    private String description = null;

    @ManyToMany
    private List<Person> persons = new ArrayList<>();

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<Person> getPersons() {
        return persons;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPersons(List<Person> persons) {
        this.persons = persons;
    }
}
