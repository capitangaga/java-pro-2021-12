package ru.otus.crm.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.crm.model.Client;

/**
 * @author kirillgolovko
 */
@Repository
public interface DBServiceClientRepo extends CrudRepository<Client, Long>, DBServiceClient {
    @Override
    @Transactional
    default Client saveClient(Client client) {
        return save(client);
    }

    @Override
    @Transactional
    default Optional<Client> getClient(long id) {
        return findById(id);
    }

    @Override
    @Transactional
    List<Client> findAll();
}
