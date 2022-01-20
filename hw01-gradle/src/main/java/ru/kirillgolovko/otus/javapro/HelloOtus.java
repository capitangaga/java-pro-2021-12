package ru.kirillgolovko.otus.javapro;

import com.google.common.math.IntMath;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author kirillgolovko
 */
public class HelloOtus {
    private static final Logger log = LogManager.getLogger(HelloOtus.class);

    public static void main(String[] args) {
        log.info("Hello, Otus!");
        log.info("Will check guava");
        checkGuavaDep();
        log.info("Done");
    }

    private static void checkGuavaDep() {
        int gcd = IntMath.gcd(2021, 2022);
        log.debug("Called guava IntMath.gcd(2021, 2022), got {}", gcd);
        log.info("Guava is ok");
    }
}
