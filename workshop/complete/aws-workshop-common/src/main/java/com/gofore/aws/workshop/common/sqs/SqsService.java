package com.gofore.aws.workshop.common.sqs;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import com.gofore.aws.workshop.common.async.ShutdownHelper;
import org.restlet.service.Service;

public class SqsService extends Service {
    
    private final SqsClient sqsClient;
    private final String queueUrl;
    private final ExecutorService executor;
    private final List<Consumer<Message>> handlers;
    private Thread thread;

    public SqsService(SqsClient sqsClient, String queueUrl, ExecutorService executor) {
        this.sqsClient = sqsClient;
        this.queueUrl = queueUrl;
        this.executor = executor;
        this.handlers = new ArrayList<>();
        ShutdownHelper.addShutdownHook(() -> this.executor);
    }

    public SqsService addHandler(Consumer<Message> messageHandler) {
        this.handlers.add(messageHandler);
        return this;
    }
    
    @Override
    public synchronized void start() throws Exception {
        if (isEnabled() && thread == null) {
            this.thread = new Thread(() -> {
                while (isStarted()) {
                    ReceiveMessageRequest request = new ReceiveMessageRequest(queueUrl).withWaitTimeSeconds(20);
                    ReceiveMessageResult result = sqsClient.receiveMessage(request).join();
                    result.getMessages().forEach(this::handleMessage);
                }
            });
            this.thread.start();
        }
        super.start();
    }

    @Override
    public synchronized void stop() throws Exception {
        super.stop();
        if (thread != null) {
            thread.join();
            thread = null;
        }
    }
    
    private void handleMessage(Message message) {
        handlers.forEach(h -> consume(h, message));
    }
    
    private void consume(Consumer<Message> handler, Message message) {
        executor.submit(() -> handler.accept(message));
    }
}
