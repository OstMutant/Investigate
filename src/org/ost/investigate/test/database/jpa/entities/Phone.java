package org.ost.investigate.test.database.jpa.entities;

import org.ost.investigate.test.database.jpa.dictionary.OperatorType;

import javax.persistence.*;

@Entity
@Inheritance(strategy= InheritanceType.JOINED)
@DiscriminatorColumn(name="operatorType")
@Table(name = "phone")
public class Phone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String number = null;
    @Column(insertable = false, updatable = false)
    private String operatorType;

    @ManyToOne
    private Person person = null;

    public void setId(int id) {
        this.id = id;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public void setOperatorType(OperatorType operatorType) {
        this.operatorType = operatorType.toString();
    }

    public OperatorType getGender() {
        return OperatorType.valueOf(operatorType);
    }

}
