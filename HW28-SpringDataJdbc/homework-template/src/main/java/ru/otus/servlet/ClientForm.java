package ru.otus.servlet;

import java.util.Optional;

/**
 * @author kirillgolovko
 */
class ClientForm {
    private final String name;
    private final String address;
    private final String phones;

    public ClientForm(String name, String address, String phones) {
        this.name = name;
        this.address = address;
        this.phones = phones;
    }

    public Optional<String> getName() {
        return Optional.ofNullable(name);
    }

    public Optional<String> getAddress() {
        return Optional.ofNullable(address);
    }

    public Optional<String> getPhones() {
        return Optional.ofNullable(phones);
    }

}
