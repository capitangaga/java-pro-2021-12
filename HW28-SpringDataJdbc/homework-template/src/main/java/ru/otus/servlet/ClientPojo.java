package ru.otus.servlet;

import java.util.List;

/**
 * @author kirillgolovko
 */
public class ClientPojo {
    private final long id;
    private final String name;
    private final String address;
    private final List<String> phones;

    public ClientPojo(long id, String name, String address, List<String> phones) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phones = phones;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public List<String> getPhones() {
        return phones;
    }
}
