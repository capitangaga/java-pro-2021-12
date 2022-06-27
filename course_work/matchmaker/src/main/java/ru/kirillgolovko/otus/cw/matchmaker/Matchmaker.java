package ru.kirillgolovko.otus.cw.matchmaker;

import com.fasterxml.jackson.databind.deser.DataFormatReaders;
import ru.kirillgolovko.cw.common.model.matchmaking.Match;

import java.util.Optional;

public interface Matchmaker {
    Optional<Match> getMatchForUser(String username);
}
