package com.restful.client.example;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.Random;

public class WebSocketStreamDeviceWholeMessageClient implements Runnable {

    private final String websocketURL;
    private Log log = LogFactory.getLog(WebSocketStreamDeviceWholeMessageClient.class);

    public WebSocketStreamDeviceWholeMessageClient(String websocketURL) {
        this.websocketURL = websocketURL;
    }

    @Override
    public void run() {
        log.info(">>> " + getClass().getSimpleName() + " BEGIN");
        try {
            URI url = new URI(websocketURL + "websocket-stream-device");
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            Session session = container.connectToServer(WebsocketClientEndpoint.class, url);
            session.addMessageHandler(new MessageHandler.Whole<byte[]>() {
                @Override
                public void onMessage(byte[] wholeMessage) {
                    log.info("------ #wholeMessage: " + wholeMessage.length);
                }
            });
            // send server some bytes
            byte[] bytes = new byte[140960];
            new Random().nextBytes(bytes);
            session.getAsyncRemote().sendBinary(ByteBuffer.wrap(bytes));
            Thread.sleep(2000);
            new Random().nextBytes(bytes);
            session.getAsyncRemote().sendBinary(ByteBuffer.wrap(bytes));
            Thread.sleep(2000);
            new Random().nextBytes(bytes);
            session.getAsyncRemote().sendBinary(ByteBuffer.wrap(bytes));
            Thread.sleep(2000);
            session.close();

        } catch (IOException | DeploymentException | URISyntaxException | InterruptedException e) {
            log.error("Exception e: " + e.getMessage());
        }
        log.info("<<< " + getClass().getSimpleName() + " END");
    }

}

