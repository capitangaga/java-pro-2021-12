package ru.kirillgolovko.otus.cw.matchmaker.server;

import java.util.Optional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.kirillgolovko.cw.common.model.matchmaking.Match;
import ru.kirillgolovko.cw.common.model.matchmaking.MatchmakingResult;
import ru.kirillgolovko.otus.cw.matchmaker.HostChooser;
import ru.kirillgolovko.otus.cw.matchmaker.Matchmaker;

@RestController
public class MatchmakerController {
    public final Matchmaker matchmaker;
    public final HostChooser hostChooser;

    public MatchmakerController(
            Matchmaker matchmaker,
            HostChooser hostChooser)
    {
        this.matchmaker = matchmaker;
        this.hostChooser = hostChooser;
    }

    @GetMapping(value = "/find-game/{username}")
    public Mono<MatchmakingResult> findGame(@PathVariable String username) {
        Optional<Match> matchForUser = matchmaker.getMatchForUser(username);
        MatchmakingResult result = new MatchmakingResult(matchForUser.orElse(null), hostChooser.getNextGameHost());
        return Mono.just(result);
    }
}
