package ru.kirillgolovko.otus.cw.client;

import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;

/**
 * @author kirillgolovko
 */
public class TestMain {
    public static void main(String[] args) throws IOException, InterruptedException {
//        List<Point> pointsToExplore = List.of(new Point(0, 0), new Point(0, 1), new Point(1, 1), new Point(1, 0));
//        Terminal terminal = new DefaultTerminalFactory().createTerminal();
//        TerminalGame terminalGame = new TerminalGame(terminal);
//        terminalGame.addKeyboardEventsConsumer(System.out::println);
//        terminalGame.startGame();
//
//        int i = 0;
//        while (true) {
//            ++ i;
//            Point point = pointsToExplore.get(i % 4);
//            GameFieldState gameFieldState = new GameFieldState(point, 0.5, 0.5, 0, 0);
//            terminalGame.updateFieldState(gameFieldState);
//            Thread.sleep(1000);
//        }

//        DefaultTerminalFactory defaultTerminalFactory = new DefaultTerminalFactory();
//        Terminal left = defaultTerminalFactory.createTerminal();
//        Terminal right = defaultTerminalFactory.createTerminal();
//        TerminalGame leftGame = new TerminalGame(left);
//        TerminalGame rightGame = new TerminalGame(right);
////        leftGame.startGame();
////        rightGame.startGame();
//        GameServer gameServer = new GameServer(leftGame, rightGame, "", GameServerSettings.DEFAULT_SETTINGS);
//        gameServer.init();
//        gameServer.start();
//        gameServer.join();
// Setup terminal and screen layers
        Terminal terminal = new DefaultTerminalFactory().createTerminal();
        Screen screen = new TerminalScreen(terminal);
        screen.startScreen();

        // Setup WindowBasedTextGUI for dialogs
        final WindowBasedTextGUI textGUI = new MultiWindowTextGUI(screen);
        MessageDialog.showMessageDialog(textGUI, "Message", "Here is a message dialog!");
        System.out.println("Ok");
    }
}
