package ru.otus.crm.model;

import java.util.List;

/**
 * @author kirillgolovko
 */
public class Clients {
    private List<Client> clients;

    public Clients() {}

    public Clients(List<Client> clients) {
        this.clients = clients;
    }

    public List<Client> getClients() {
        return clients;
    }

    public void setClients(List<Client> clients) {
        this.clients = clients;
    }
}
