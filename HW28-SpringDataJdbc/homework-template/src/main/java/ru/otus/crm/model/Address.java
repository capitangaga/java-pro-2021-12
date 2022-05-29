package ru.otus.crm.model;

import org.springframework.data.annotation.Id;

/**
 * @author kirillgolovko
 */
public class Address implements Cloneable{

    @Id
    private Long id;
    private String street;

    public Address(String street) {
        this.id = null;
        this.street = street;
    }

    public Address(Long id, String street) {
        this.id = id;
        this.street = street;
    }

    public Address() {
        this.id = null;
        this.street = null;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new Address(id, street);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", street='" + street + '\'' +
                '}';
    }
}
