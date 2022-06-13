package ru.otus.servlet.dbclient;

import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Clients;

/**
 * @author kirillgolovko
 */
public class ClientsDbReactiveClientImpl implements ClientsDbReactiveClient {
    private final WebClient webClient;

    public ClientsDbReactiveClientImpl(String host, int port) {
        this.webClient = WebClient.builder().baseUrl(String.format("http://%s:%d", host, port)).build();
    }

    @Override
    public Mono<Clients> findAll() {
        return webClient.get().uri("/clients-db/findAll")
                .retrieve()
                .bodyToMono(Clients.class);
    }

    @Override
    public Mono<Client> save(Mono<Client> client) {
        return webClient.post().uri("clients-db/save")
                .body(client, Client.class)
                .retrieve()
                .bodyToMono(Client.class);
    }


}
