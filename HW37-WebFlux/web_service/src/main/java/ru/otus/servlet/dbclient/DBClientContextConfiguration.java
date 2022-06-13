package ru.otus.servlet.dbclient;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author kirillgolovko
 */
@Configuration
public class DBClientContextConfiguration {
    @Bean
    ClientsDbReactiveClient clientsDbReactiveClient() {
        return new ClientsDbReactiveClientImpl("localhost", 8090);
    }
}
