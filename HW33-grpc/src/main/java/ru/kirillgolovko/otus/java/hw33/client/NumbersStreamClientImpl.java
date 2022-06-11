package ru.kirillgolovko.otus.java.hw33.client;

import java.util.Optional;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import ru.kirillgolovko.otus.java.hw33.generated.NextNumber;
import ru.kirillgolovko.otus.java.hw33.generated.NumberServiceGrpc;
import ru.kirillgolovko.otus.java.hw33.generated.NumbersRequest;

/**
 * @author kirillgolovko
 */
public class NumbersStreamClientImpl implements NumbersStreamClient {

    private final ManagedChannel channel;
    private volatile long lastNumber = 0;
    private volatile boolean gotUnread = false;

    public NumbersStreamClientImpl(String host, int port) {
        this.channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
    }

    @Override
    public void startGettingNumbers(long from, long to) {
        // reset client
        gotUnread = false;
        NumberServiceGrpc.NumberServiceStub stub = NumberServiceGrpc.newStub(channel);
        stub.streamNumbers(
                NumbersRequest.newBuilder().setFrom(from).setTo(to).build(),
                new StreamObserver<NextNumber>() {
                    @Override
                    public void onNext(NextNumber value) {
                        synchronized (this) {
                            lastNumber = value.getNumber();
                            gotUnread = true;
                        }
                        System.out.printf(
                                "[%s] Got new number from server %d\n",
                                Thread.currentThread().getName(),
                                value.getNumber());
                    }

                    @Override
                    public void onError(Throwable t) {
                        System.err.println(t);
                    }

                    @Override
                    public void onCompleted() {
                        System.out.println("Stream finished");
                    }
        });

    }

    @Override
    public Optional<Long> getLastNumber() {
        Optional<Long> result = Optional.empty();
        synchronized (this) {
            if (gotUnread) {
                result = Optional.of(lastNumber);
                gotUnread = false;
            }
        }
        return result;
    }

    @Override
    public void close() {
        channel.shutdown();
    }
}
