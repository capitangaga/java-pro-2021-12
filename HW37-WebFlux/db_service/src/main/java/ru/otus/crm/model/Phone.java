package ru.otus.crm.model;

import org.springframework.data.annotation.Id;

/**
 * @author kirillgolovko
 */
public class Phone implements Cloneable {

    @Id
    private Long id;
    private String number;

    public Phone() {
        this(null, null);
    }

    public Phone(Long id, String number) {
        this.id = id;
        this.number = number;

    }

    public Phone(String number) {
        this(null, number);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new Phone(id, number);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }


    @Override
    public String toString() {
        return "Phone{" +
                "id=" + id +
                ", number='" + number + '\'' +
                '}';
    }
}
