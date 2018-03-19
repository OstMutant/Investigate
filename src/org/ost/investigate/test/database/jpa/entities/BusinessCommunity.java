package org.ost.investigate.test.database.jpa.entities;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "business_community")
public class BusinessCommunity extends Community{
    private String requirement = null;

    public void setRequirement(String requirement) {
        this.requirement = requirement;
    }

    public String getRequirement() {
        return requirement;
    }
}
