package com.restful.client.example;

import com.fererlab.async.dto.AsyncRequestDTO;
import com.fererlab.async.dto.AsyncResponseDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

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
        ResteasyWebTarget target = client.target(url).path("/async/sayHi");
        AsyncRequestDTO asyncRequestDTO = new AsyncRequestDTO();
        asyncRequestDTO.setRequest("john");
        Entity entity = Entity.entity(asyncRequestDTO, MediaType.APPLICATION_JSON);
        target.request().async().post(
                entity,
                new InvocationCallback<AsyncResponseDTO>() {
                    @Override
                    public void completed(AsyncResponseDTO response) {
                        log.info(response.getResponse());
                    }

                    @Override
                    public void failed(Throwable throwable) {
                        log.info("Exception: " + throwable.getMessage());
                    }
                });
        log.info("<<< " + getClass().getSimpleName() + " END");
    }
}
