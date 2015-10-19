package com.restful.client.example;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketSubscriber implements Runnable {

    private final String websocketURL;
    private Log log = LogFactory.getLog(WebSocketSubscriber.class);

    public WebSocketSubscriber(String websocketURL) {
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
                    log.info("------ message received from publisher: " + message);
                    // if the message is EOL then close subscriber
                    if ("EOL".equals(message)) {
                        try {
                            log.info("------ subscriber closes");
                            session.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        } catch (IOException | DeploymentException | URISyntaxException e) {
            log.error("Exception e: " + e.getMessage());
        }
        log.info("<<< " + getClass().getSimpleName() + " END");
    }

}

