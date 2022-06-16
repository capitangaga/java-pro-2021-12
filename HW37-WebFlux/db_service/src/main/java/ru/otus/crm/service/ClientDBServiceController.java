package ru.otus.crm.service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Clients;

/**
 * @author kirillgolovko
 */
@RestController
public class ClientDBServiceController {
    public final ClientsDBAsyncClient dbClient;

    public ClientDBServiceController(ClientsDBAsyncClient dbClient) {
        this.dbClient = dbClient;
    }

    @GetMapping(path = "clients-db/findAll")
    public Mono<Clients> findAll() {
        return Mono.fromFuture(dbClient.findAll()).map(Clients::new);
    }

    @PostMapping(path = "clients-db/save")
    public Mono<Client> save(@RequestBody Client client) {
        return Mono.fromFuture(dbClient.saveClient(client));
    }
}
