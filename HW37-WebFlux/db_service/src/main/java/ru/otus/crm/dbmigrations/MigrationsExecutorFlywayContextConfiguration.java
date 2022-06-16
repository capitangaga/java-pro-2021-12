package ru.otus.crm.dbmigrations;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

/**
 * @author kirillgolovko
 */
public class MigrationsExecutorFlywayContextConfiguration {
    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.username}")
    private String user;
    @Value("${spring.datasource.password}")
    private String pwd;

    @PostConstruct
    public void doMigrations() {
        new MigrationsExecutorFlyway(url, user, pwd).executeMigrations();
    }
}
