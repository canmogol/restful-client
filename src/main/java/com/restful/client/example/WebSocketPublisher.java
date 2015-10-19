package com.restful.client.example;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketPublisher implements Runnable {

    private final String websocketURL;
    private Log log = LogFactory.getLog(WebSocketPublisher.class);

    public WebSocketPublisher(String websocketURL) {
        this.websocketURL = websocketURL;
    }

    @Override
    public void run() {
        log.info(">>> " + getClass().getSimpleName() + " BEGIN");
        try {
            URI url = new URI(websocketURL + "websocket-pubsub");
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            ClientEndpointConfig clientEndpointConfig = ClientEndpointConfig.Builder.create().build();
            Session session = container.connectToServer(WebsocketClientEndpoint.class, url);
            session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    log.info("------ message received from other publishers: " + message);
                }
            });
            // send 10 messages
            for (int i = 0; i < 5; i++) {
                Thread.sleep(1000);
                session.getAsyncRemote().sendText("current number: " + i);
            }
            // send End Of Line message so the subscribers close too
            log.info("------ publisher sends EOL");
            session.getAsyncRemote().sendText("EOL");
            log.info("------ publisher closes");
            session.close();

        } catch (IOException | DeploymentException | URISyntaxException | InterruptedException e) {
            log.error("Exception e: " + e.getMessage());
        }
        log.info("<<< " + getClass().getSimpleName() + " END");
    }

}

