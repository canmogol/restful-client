package com.restful.client.example;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.jsontype.TypeResolverBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.plugins.providers.jackson.ResteasyJacksonProvider;

import javax.ws.rs.client.AsyncInvoker;
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
        ResteasyClient client = new ResteasyClientBuilder().build();

        // add @CLASS property to requested json
        ResteasyJacksonProvider resteasyJacksonProvider = new ResteasyJacksonProvider();
        ObjectMapper mapper = new ObjectMapper();
        TypeResolverBuilder<?> typeResolver = new CustomTypeResolverBuilder();
        typeResolver.init(JsonTypeInfo.Id.CLASS, null);
        typeResolver.inclusion(JsonTypeInfo.As.PROPERTY);
        typeResolver.typeProperty("@CLASS");
        mapper.setDefaultTyping(typeResolver);
        resteasyJacksonProvider.setMapper(mapper);
        client.register(resteasyJacksonProvider);

        WebTarget target = client.target(url);
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
