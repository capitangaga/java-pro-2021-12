package ru.kirillgolovko.otus.cw.matchmaker;

import org.springframework.stereotype.Component;

/**
 * @author kirillgolovko
 */
@Component
public class LocalhostHostChooser implements HostChooser {
    @Override
    public String getNextGameHost() {
        return "localhost:8080";
    }
}
