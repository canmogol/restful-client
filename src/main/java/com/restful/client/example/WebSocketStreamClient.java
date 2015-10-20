package com.restful.client.example;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.Random;

public class WebSocketStreamClient implements Runnable {

    private final String websocketURL;
    private Log log = LogFactory.getLog(WebSocketStreamClient.class);

    public WebSocketStreamClient(String websocketURL) {
        this.websocketURL = websocketURL;
    }

    @Override
    public void run() {
        log.info(">>> " + getClass().getSimpleName() + " BEGIN");
        try {
            URI url = new URI(websocketURL + "websocket-stream");
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            Session session = container.connectToServer(WebsocketClientEndpoint.class, url);
            session.addMessageHandler(new MessageHandler.Partial<byte[]>() {
                @Override
                public void onMessage(byte[] partialMessage, boolean last) {
                    log.info("------ #partialMessage: " + partialMessage.length + " last: " + last);
                }
            });
            // send server some bytes
            byte[] bytes = new byte[140960];
            new Random().nextBytes(bytes);
            session.getAsyncRemote().sendBinary(ByteBuffer.wrap(bytes));
            session.getAsyncRemote().sendBinary(ByteBuffer.wrap(bytes));
            session.getAsyncRemote().sendBinary(ByteBuffer.wrap(bytes));
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            session.getAsyncRemote().sendBinary(ByteBuffer.wrap(new byte[]{1}));

        } catch (IOException | DeploymentException | URISyntaxException e) {
            log.error("Exception e: " + e.getMessage());
        }
        log.info("<<< " + getClass().getSimpleName() + " END");
    }

}

