package com.restful.client.example;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * websocket client
 */
public class WebSocketSingleClient implements Runnable {

    private final String websocketURL;
    private Log log = LogFactory.getLog(WebSocketSingleClient.class);

    public WebSocketSingleClient(String websocketURL) {
        this.websocketURL = websocketURL;
    }

    @Override
    public void run() {
        log.info(">>> " + getClass().getSimpleName() + " BEGIN");
        try {
            URI url = new URI(websocketURL + "websocket-single");
            WebSocketContainer container = javax.websocket.ContainerProvider.getWebSocketContainer();
            Session session = container.connectToServer(WebsocketClientEndpoint.class, url);
            session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    log.info("------ message received from server: " + message);
                    try {
                        session.close();
                        log.info("------ closed");
                    } catch (IOException e) {
                        log.error("Exception e: " + e.getMessage());
                    }
                }
            });
            session.getBasicRemote().sendText("Hi from java");

        } catch (IOException | DeploymentException | URISyntaxException e) {
            log.error("Exception e: " + e.getMessage());
        }
        log.info("<<< " + getClass().getSimpleName() + " END");
    }

}

