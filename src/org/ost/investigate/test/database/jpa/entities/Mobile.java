package org.ost.investigate.test.database.jpa.entities;

import org.ost.investigate.test.database.jpa.dictionary.OperatorType;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity()
@DiscriminatorValue(OperatorType.Values.MOBILE)
@Table(name = "mobile")
public class Mobile extends Phone {
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private Date start;

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getStart() {

        return start;
    }
}
