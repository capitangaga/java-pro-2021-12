package ru.kirillgolovko.otus.cw.client.cli;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.function.Consumer;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.Terminal;
import ru.kirillgolovko.cw.common.game.client.GameClient;
import ru.kirillgolovko.cw.common.model.game.GameFieldState;
import ru.kirillgolovko.cw.common.model.game.Point;
import ru.kirillgolovko.cw.common.model.game.KeyboardEvent;
import ru.kirillgolovko.cw.common.model.game.KeyboardEventType;


/**
 * @author kirillgolovko
 */
public class TerminalGame implements GameClient {

    private final Thread keysPublisherThread;
    private final Thread keysCallbacksThread;
    private final Thread drawingThread;

    private final BlockingDeque<KeyboardEvent> keyboardEventsQueue;
    private final BlockingDeque<GameFieldState> fieldStateQueue;

    private final List<Consumer<KeyboardEvent>> keyboardEventConsumers;

    public TerminalGame (Terminal terminal) {
        keyboardEventsQueue = new LinkedBlockingDeque<>(1000);
        fieldStateQueue = new LinkedBlockingDeque<>(1000);
        keyboardEventConsumers = new ArrayList<>();

        keysPublisherThread = getKeyStrokePublisherThread(keyboardEventsQueue, terminal);
        keysCallbacksThread = getKeyboardCallbackThread(keyboardEventsQueue, keyboardEventConsumers);
        drawingThread = getDrawingThread(fieldStateQueue, terminal);
    }

    @Override
    public void startGame() {
        keysCallbacksThread.start();
        keysPublisherThread.start();
        drawingThread.start();
    }

    @Override
    public void stopGame() {
        drawingThread.interrupt();
        keysPublisherThread.interrupt();
        keysCallbacksThread.interrupt();
    }

    @Override
    public void addKeyboardEventsConsumer(Consumer<KeyboardEvent> consumer) {
        keyboardEventConsumers.add(consumer);
    }

    @Override
    public void removeKeyboardEventsConsumer(Consumer<KeyboardEvent> consumer) {
        keyboardEventConsumers.remove(consumer);
    }

    @Override
    public boolean updateFieldState(GameFieldState gameFieldState) {
        return fieldStateQueue.offer(gameFieldState);
    }

    private static Thread getKeyStrokePublisherThread(BlockingDeque<KeyboardEvent> publishingQueue, Terminal terminal) {
        return new Thread(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    KeyStroke keyStroke = terminal.readInput();
                    long time = System.currentTimeMillis();
                    Optional<KeyboardEvent> event = switch (keyStroke.getKeyType()) {
                        case ArrowUp -> Optional.of(new KeyboardEvent(time, KeyboardEventType.ARROW_UP));
                        case ArrowDown -> Optional.of(new KeyboardEvent(time, KeyboardEventType.ARROW_DOWN));
                        default -> Optional.empty();
                    };
                    event.ifPresent(publishingQueue::add);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private static Thread getKeyboardCallbackThread(
            BlockingDeque<KeyboardEvent> publishingQueue,
            List<Consumer<KeyboardEvent>> consumers)
    {
        return new Thread(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    KeyboardEvent nextEvent = publishingQueue.take();
                    consumers.forEach(consumer -> consumer.accept(nextEvent));
                }
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        });
    }

    private static Thread getDrawingThread(BlockingDeque<GameFieldState> fieldStateQueue, Terminal terminal) {
        try {
            Screen screen = new TerminalScreen(terminal);
            screen.startScreen();
            TextGraphics textGraphics = screen.newTextGraphics();
            return new Thread(() -> {
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        GameFieldState lastState = fieldStateQueue.take();
                        TerminalSize terminalSize = screen.getTerminalSize();

                        convertToTerminalCoordinates(terminalSize, lastState.getBallPosition());
                        screen.clear();

                        drawBall(terminalSize, textGraphics, lastState);
                        drawPads(terminalSize, textGraphics, lastState);
                        drawScore(textGraphics, lastState);

                        screen.refresh(Screen.RefreshType.COMPLETE);


                        Thread.yield();
                    } catch (InterruptedException | IOException ex) {
                        throw new RuntimeException(ex);
                    }

                }
            });
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private static void drawBall(TerminalSize terminalSize, TextGraphics graphics, GameFieldState gameFieldState) {
        TerminalPosition ballPos = convertToTerminalCoordinates(terminalSize, gameFieldState.getBallPosition());
        graphics.drawRectangle(ballPos, TerminalSize.ONE, '*');
    }

    private static void drawPads(TerminalSize terminalSize, TextGraphics graphics, GameFieldState gameFieldState) {
        TerminalPosition leftPad
                = convertToTerminalCoordinates(terminalSize, new Point(0, gameFieldState.getLeftHandle()));
        TerminalPosition rightPad
                = convertToTerminalCoordinates(terminalSize, new Point(1, gameFieldState.getRightHandle()));

        graphics.setForegroundColor(TextColor.ANSI.YELLOW_BRIGHT);
        graphics.drawRectangle(leftPad.withRelativeRow(-1), new TerminalSize(1, 3), '#');
        graphics.setForegroundColor(TextColor.ANSI.RED_BRIGHT);
        graphics.drawRectangle(rightPad.withRelativeRow(-1), new TerminalSize(1, 3), '#');
        graphics.setForegroundColor(TextColor.ANSI.WHITE);
    }

    private static void drawScore(TextGraphics textGraphics, GameFieldState gameFieldState) {
        textGraphics.putString(TerminalPosition.TOP_LEFT_CORNER, String.format("Score: %d : %d", gameFieldState.getLeftScore(), gameFieldState.getRightScore()));
    }

    private static TerminalPosition convertToTerminalCoordinates(TerminalSize terminalSize, Point point) {
        int tWidth = terminalSize.getColumns() - 1;
        int tHeight = terminalSize.getRows() - 2;

        int newX = (int) Math.round(tWidth * point.getX());
        int newY = (int) (1 + Math.round(tHeight * (1 - point.getY())));

        return new TerminalPosition(newX, newY);
    }

}
