package com.restful.client.example;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ws.rs.client.AsyncInvoker;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * async(non-blocking) server side and blocking client side example
 */
public class AsyncServerBlockingClient implements Runnable {

    private Log log = LogFactory.getLog(AsyncServerBlockingClient.class);

    private final String url;

    public AsyncServerBlockingClient(String url) {
        this.url = url;
    }

    @Override
    public void run() {
        log.info(">>> " + getClass().getSimpleName() + " BEGIN");
        WebTarget target = ClientBuilder.newClient().target(url);
        AsyncInvoker asyncInvoker = target.path("/async/executorRunnable").request().async();
        Entity entity = Entity.entity("John", MediaType.APPLICATION_JSON);
        Future<String> responseFuture = asyncInvoker.post(entity, String.class);
        try {
            String response = responseFuture.get();
            responseFuture.get(10, TimeUnit.SECONDS); // block for max 10 seconds
            log.info("got response: " + response);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            log.info("Exception: " + e.getMessage());
        }
        log.info("<<< " + getClass().getSimpleName() + " END");
    }
}
