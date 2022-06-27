package ru.kirillgolovko.otus.cw.matchmaker.server;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.kirillgolovko.cw.common.model.matchmaking.Match;
import ru.kirillgolovko.otus.cw.matchmaker.Matchmaker;

@RestController
public class MatchmakerController {
    public final Matchmaker matchmaker;

    public MatchmakerController(Matchmaker matchmaker) {
        this.matchmaker = matchmaker;
    }

    @GetMapping(value = "/find-game/{username}")
    public Mono<Match> findGame(@PathVariable String username) {
        return Mono.justOrEmpty(matchmaker.getMatchForUser(username));
    }
}
