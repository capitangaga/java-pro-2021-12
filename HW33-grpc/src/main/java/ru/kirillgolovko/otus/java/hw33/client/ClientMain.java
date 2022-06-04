package ru.kirillgolovko.otus.java.hw33.client;

/**
 * @author kirillgolovko
 */
public class ClientMain {
    public static void main(String[] args) {
        try (NumbersStreamClient client = new NumbersStreamClientImpl("localhost", 8081);) {
            client.startGettingNumbers(1, 30);
            NumberPrinter.printNumbers(0, 50, client::getLastNumber);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
