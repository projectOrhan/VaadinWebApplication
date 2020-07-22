package com.vaadin.assignment.backend.entity;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
public class Customer extends AbstractEntity implements Cloneable {
    public enum Gender {
        Male,Female
    }

    public enum Flag {
        Active, Passive
    }

    @NotNull
    @NotEmpty
    private String firstName = "";

    @NotNull
    @NotEmpty
    private String lastName = "";

    @NotNull
    private LocalDate birthDate ;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "company_id")
    private BirthCity birthCity;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Customer.Gender gender;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Customer.Flag flag;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public BirthCity getBirthCity() {
        return birthCity;
    }

    public void setBirthCity(BirthCity birthCity) {
        this.birthCity = birthCity;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Flag getFlag() {
        return flag;
    }

    public void setFlag(Flag flag) {
        this.flag = flag;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }

}
