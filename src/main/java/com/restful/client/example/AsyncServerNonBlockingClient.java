package com.restful.client.example;

import com.fererlab.async.dto.AsyncRequestDTO;
import com.fererlab.async.dto.AsyncResponseDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.jsontype.TypeResolverBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.plugins.providers.jackson.ResteasyJacksonProvider;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.InvocationCallback;
import javax.ws.rs.core.MediaType;

/**
 * async(non-blocking) server side and non-blocking client side example
 */
public class AsyncServerNonBlockingClient implements Runnable {

    private Log log = LogFactory.getLog(AsyncServerNonBlockingClient.class);

    private final String url;

    public AsyncServerNonBlockingClient(String url) {
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

        ResteasyWebTarget target = client.target(url).path("/async/sayHi");
        AsyncRequestDTO asyncRequestDTO = new AsyncRequestDTO();
        asyncRequestDTO.setRequest("john");
        Entity entity = Entity.entity(asyncRequestDTO, MediaType.APPLICATION_JSON);
        target.request().async().post(
                entity,
                new InvocationCallback<AsyncResponseDTO>() {
                    @Override
                    public void completed(AsyncResponseDTO response) {
                        log.info("Received AsyncResponseDTO: "+response.getResponse());
                    }

                    @Override
                    public void failed(Throwable throwable) {
                        log.info("Received Exception: " + throwable.getMessage());
                    }
                });
        log.info("<<< " + getClass().getSimpleName() + " END");
    }
}
