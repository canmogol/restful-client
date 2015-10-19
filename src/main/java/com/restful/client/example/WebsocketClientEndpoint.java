package com.restful.client.example;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.websocket.*;

@ClientEndpoint
public class WebsocketClientEndpoint {

    private Log log = LogFactory.getLog(WebsocketClientEndpoint.class);

    @OnOpen
    public void onOpen(final Session session, EndpointConfig config) {
        log.info("OPEN: this client's session: " + session.getId());
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        log.info("CLOSE: this client's session: " + session.getId());
    }

    @OnError
    public void onError(Session session, Throwable t) {
        log.info("EXCEPTION: " + t.getMessage() + " this client's session:: " + session.getId());
        t.printStackTrace();
    }

}
