package com.vaadin.assignment.backend.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.LinkedList;
import java.util.List;

@Entity
public class BirthCity extends AbstractEntity {
    private String name;

    @OneToMany (mappedBy = "birthCity", fetch = FetchType.EAGER)
    private List<Customer> customers = new LinkedList<>();

    public BirthCity() {

    }

    public BirthCity(String name) {
        setName(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Customer> getCustomers() {
        return customers;
    }
}
