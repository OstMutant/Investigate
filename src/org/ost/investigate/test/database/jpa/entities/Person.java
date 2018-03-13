package org.ost.investigate.test.database.jpa.entities;

import org.ost.investigate.test.database.jpa.dictionary.Gender;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="gender", discriminatorType=DiscriminatorType.STRING)
@Table(name = "person")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name = null;
    private String email = null;
    private String idCode = null;

    @Enumerated(EnumType.STRING)
    protected Gender gender;
    @OneToMany
    private List<Phone> phones = new ArrayList<>();
    @ManyToMany
    private List<Community> communities = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdCode() {
        return idCode;
    }

    public void setIdCode(String idCode) {
        this.idCode = idCode;
    }

    public List<Phone> getPhones() {
        return phones;
    }

    public void setPhones(List<Phone> phones) {
        this.phones = phones;
    }

    public void setCommunities(List<Community> communities) {
        this.communities = communities;
    }

    public List<Community> getCommunities() {
        return communities;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Gender getGender() {
        return gender;
    }
}
