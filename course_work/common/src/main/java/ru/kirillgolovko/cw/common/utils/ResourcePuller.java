package ru.kirillgolovko.cw.common.utils;

import java.util.Optional;
import java.util.function.Function;


/**
 * @author kirillgolovko
 */
public class ResourcePuller<T, R> implements Function<T, Optional<R>> {
    private final Function<T, Optional<R>> innerFunction;
    private final long poolIntervalMs;
    private final long timeout;

    public ResourcePuller(Function<T, Optional<R>> innerFunction, long poolIntervalMs, long timeout) {
        this.innerFunction = innerFunction;
        this.poolIntervalMs = poolIntervalMs;
        this.timeout = timeout;
    }

    @Override
    public Optional<R> apply(T argument) {
        long startTs = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTs <= timeout) {
            Optional<R> res = innerFunction.apply(argument);
            if (res.isPresent()) {
                return res;
            }
            try {
                Thread.sleep(poolIntervalMs);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return Optional.empty();
    }
}
