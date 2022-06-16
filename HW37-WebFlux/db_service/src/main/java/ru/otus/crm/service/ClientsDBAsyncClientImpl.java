package ru.otus.crm.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.stereotype.Component;
import ru.otus.crm.model.Client;

/**
 * @author kirillgolovko
 */
@Component
public class ClientsDBAsyncClientImpl implements ClientsDBAsyncClient {

    private final DBServiceClient dbServiceClient;

    private ExecutorService executorService;

    public ClientsDBAsyncClientImpl(DBServiceClient dbServiceClient) {
        this.dbServiceClient = dbServiceClient;
    }

    @PostConstruct
    public void start() {
        executorService = Executors.newFixedThreadPool(8);
    }

    @PreDestroy
    public void stop() {
        executorService.shutdown();
    }

    @Override
    public CompletableFuture<List<Client>> findAll() {
        return CompletableFuture.supplyAsync(dbServiceClient::findAll, executorService);
    }

    @Override
    public CompletableFuture<Client> saveClient(Client client) {
        return CompletableFuture.supplyAsync(() -> dbServiceClient.saveClient(client), executorService);
    }
}
