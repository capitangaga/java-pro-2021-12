package ru.otus.test22;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.shaded.org.apache.commons.lang.time.StopWatch;
import ru.otus.base.AbstractHibernateTest;
import ru.otus.cachehw.HwCache;
import ru.otus.cachehw.MyCache;
import ru.otus.crm.model.Client;
import ru.otus.crm.service.DBServiceCachedClient;

/**
 * @author kirillgolovko
 */
public class CacheTest extends AbstractHibernateTest {

    @BeforeEach
    @Override
    public void setUp() {
        super.setUp();
        for(long i = 0; i < 1000; ++i) {
            Client client = new Client(Long.toString(i + 1));
            dbServiceClient.saveClient(client);
        }
    }

    @Test
    public void testPerformance() {
        HwCache<Long, Client> cache = new MyCache<>();
        DBServiceCachedClient cachedClient = new DBServiceCachedClient(dbServiceClient, cache);

        StopWatch stopWatchNotCached = new StopWatch();

        stopWatchNotCached.start();

        for (int i = 0; i < 100; ++i) {
            Optional<Client> client = dbServiceClient.getClient(500);
            Assertions.assertTrue(client.isPresent());
        }

        System.out.println("Not cached avg time per request (ms): " + stopWatchNotCached.getTime() / 100.0);
        // Я понимаю, что так писать нельзя и этот тест будет постоянно флапать, нужно исключительно для демонстрации
        Assertions.assertTrue(stopWatchNotCached.getTime() / 100.0 > 1);

        StopWatch stopWatchCached = new StopWatch();

        stopWatchCached.start();

        for (int i = 0; i < 100; ++i) {
            Optional<Client> client = cachedClient.getClient(500);
            Assertions.assertTrue(client.isPresent());
        }

        System.out.println("Cached avg time per request (ms): " + stopWatchCached.getTime() / 100.0);
        // Я понимаю, что так писать нельзя и этот тест будет постоянно флапать, нужно исключительно для демонстрации
        Assertions.assertTrue(stopWatchCached.getTime() / 100.0 < 0.2);
    }

    @Test
    public void testGcKillingCache() {
        HwCache<Long, Client> cache = new MyCache<>();


        AtomicLong addCount = new AtomicLong(0);

        cache.addListener((key, value, action) -> {
            if (action.equals("put")) {
                addCount.incrementAndGet();
            }
        });

        AtomicLong gcCount = new AtomicLong(0);

        cache.addListener(((key, value, action) -> {
            if (action.contains("Killed by gc")) {
                gcCount.incrementAndGet();
            }
        }));

        DBServiceCachedClient cachedClient = new DBServiceCachedClient(dbServiceClient, cache);

        cachedClient.findAll();
        Assertions.assertEquals(1000, addCount.get());

        System.gc();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        Assertions.assertEquals(1000, gcCount.get());
    }
}
