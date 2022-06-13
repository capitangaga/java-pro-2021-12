package ru.otus.servlet.dbclient;

import reactor.core.publisher.Mono;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Clients;

/**
 * @author kirillgolovko
 */
public interface ClientsDbReactiveClient {
    Mono<Clients> findAll();

    Mono<Client> save(Mono<Client> client);
}
