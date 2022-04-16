package ru.otus.crm.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author kirillgolovko
 */
@Entity
@Table(name = "phone")
public class Phone implements Cloneable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "client_id")
    private Client client;

    @Column(name = "number")
    private String number;

    public Phone() {
        this(null,null, null);
    }

    public Phone(Long id, Client client, String number) {
        this.id = id;
        this.client = client;
        this.number = number;

    }

    public Phone(String number) {
        this(null, null, number);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new Phone(id, client, number);
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

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public String toString() {
        return "Phone{" +
                "id=" + id +
                ", client=" + client.getName() +
                ", number='" + number + '\'' +
                '}';
    }
}
