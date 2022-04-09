package ru.otus.crm.service;

import java.util.List;
import java.util.Optional;

import ru.otus.cachehw.HwCache;
import ru.otus.crm.model.Client;

/**
 * @author kirillgolovko
 */
public class DBServiceCachedClient implements DBServiceClient {

    private final DBServiceClient baseClient;
    private final HwCache<Long, Client> hwCache;

    public DBServiceCachedClient(DBServiceClient baseClient, HwCache<Long, Client> hwCache) {
        this.baseClient = baseClient;
        this.hwCache = hwCache;
    }

    @Override
    public Client saveClient(Client client) {
        Client clientSaved = baseClient.saveClient(client);
        hwCache.put(clientSaved.getId(), clientSaved);
        return clientSaved;
    }

    @Override
    public Optional<Client> getClient(long id) {
        Client cached = hwCache.get(id);
        if (cached != null) {
            return Optional.of(cached);
        }

        Optional<Client> foundInDb = baseClient.getClient(id);
        // Не хотим ловить эффекты пула лонгов
        foundInDb.ifPresent(client -> hwCache.put(new Long(client.getId()), client));
        return foundInDb;
    }

    @Override
    public List<Client> findAll() {
        List<Client> found = baseClient.findAll();
        // Не хотим ловить эффекты пула лонгов
        found.forEach(client -> hwCache.put(new Long(client.getId()), client));
        return found;
    }
}
