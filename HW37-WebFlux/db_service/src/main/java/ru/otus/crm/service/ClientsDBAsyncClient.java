package ru.otus.crm.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import ru.otus.crm.model.Client;

/**
 * @author kirillgolovko
 */
public interface ClientsDBAsyncClient {
    CompletableFuture<List<Client>> findAll();
    CompletableFuture<Client> saveClient(Client client);
}
