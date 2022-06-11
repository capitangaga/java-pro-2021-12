package ru.kirillgolovko.otus.java.hw33.server;

import io.grpc.stub.StreamObserver;
import ru.kirillgolovko.otus.java.hw33.generated.NextNumber;
import ru.kirillgolovko.otus.java.hw33.generated.NumberServiceGrpc;
import ru.kirillgolovko.otus.java.hw33.generated.NumbersRequest;

/**
 * @author kirillgolovko
 */
public class NumbersServerImpl extends NumberServiceGrpc.NumberServiceImplBase {

    @Override
    public void streamNumbers(NumbersRequest request, StreamObserver<NextNumber> responseObserver) {
        long from = request.getFrom();
        long to = request.getTo();

        for (long i = from; i <= to; i++) {
            responseObserver.onNext(NextNumber.newBuilder().setNumber(i).build());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
         responseObserver.onCompleted();
    }
}
