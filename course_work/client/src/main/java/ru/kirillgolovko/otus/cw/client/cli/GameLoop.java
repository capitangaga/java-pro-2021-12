package ru.kirillgolovko.otus.cw.client.cli;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Pattern;

import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.Window;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import com.googlecode.lanterna.gui2.dialogs.TextInputDialogBuilder;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.Terminal;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import ru.kirillgolovko.cw.common.model.matchmaking.MatchmakingResult;
import ru.kirillgolovko.otus.cw.client.connector.StompClientConnector;

/**
 * @author kirillgolovko
 */
public class GameLoop {
    private final static String CONNECT_URL = "ws://%s/connect?game_session_id={session}&side={side}";

    private final Terminal terminal;
    private final Function<String, Optional<MatchmakingResult>> matchSupplier;
    private final WebSocketStompClient stompClient;

    private String username = null;

    public GameLoop(
            Terminal terminal,
            Function<String, Optional<MatchmakingResult>>  matchSupplier,
            WebSocketStompClient stompClient)
    {
        this.terminal = terminal;
        this.matchSupplier = matchSupplier;
        this.stompClient = stompClient;
    }

    public void runLoop() throws IOException {
        Screen screen = new TerminalScreen(terminal);
        screen.startScreen();
        while (true) {
            screen.clear();
            WindowBasedTextGUI textGUI = new MultiWindowTextGUI(screen);

            if (username == null) {
                username = requestName(textGUI);
                if (username == null) {
                    continue;
                }
            }

            showLabel(textGUI, "Wait for yor game session");

            Optional<MatchmakingResult> match = matchSupplier.apply(username);
            if (match.isEmpty()) {
                showMessageWithButton(textGUI, "Failed to get session", "Try again!");
                continue;
            }

            TerminalGame terminalGame = new TerminalGame(
                    terminal,
                    match.get().getMatch().getSide().equals("l") ? username : match.get().getMatch().getOpponentName(),
                    match.get().getMatch().getSide().equals("l") ? match.get().getMatch().getOpponentName() : username);
            try {
                ListenableFuture<StompSession> connection = stompClient.connect(
                        String.format(CONNECT_URL, match.get().getGameHost()),
                        new StompClientConnector(
                                terminalGame,
                                match.get().getMatch().getSessionId(),
                                match.get().getMatch().getSide()),
                        match.get().getMatch().getSessionId(),
                        match.get().getMatch().getSide());
                try {
                    terminalGame.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                terminalGame.stopGame();
            } catch (Exception ex) {
                showMessageWithButton(textGUI, "Game server connection failed", "Try again!");
            }
        }
    }

    private static String requestName(WindowBasedTextGUI gui) {
        return new TextInputDialogBuilder()
                .setTitle("Enter your nickname")
                .setDescription("3-20 chars")
                .setValidationPattern(Pattern.compile("[0-9A-Za-z]{3,20}"), "3 - 20 latin letters or numbers")
                .build()
                .showDialog(gui);
    }


    private static void showLabel(WindowBasedTextGUI gui, String text) {
        Label label = new Label(text);
        Panel panel = new Panel();
        panel.addComponent(label);
        BasicWindow window = new BasicWindow();
        window.setHints(Arrays.asList(Window.Hint.CENTERED));
        window.setComponent(panel);
        gui.addWindow(window);
        try {
            gui.updateScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void showMessageWithButton(WindowBasedTextGUI gui, String header, String text) {
        MessageDialog.showMessageDialog(gui, header, text, MessageDialogButton.OK);
    }

}
