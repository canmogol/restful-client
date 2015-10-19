package com.restful.client.example;


import com.fererlab.city.dto.CityIdIntegerDTO;
import com.fererlab.city.dto.CityIdLongDTO;
import com.fererlab.city.restful.CityResource;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

/**
 * blocking server and blocking client request response example with multiple models/entities
 */
public class MultipleEntityType implements Runnable {

    private Log log = LogFactory.getLog(MultipleEntityType.class);

    private final String url;

    public MultipleEntityType(String url) {
        this.url = url;
    }

    @Override
    public void run() {
        log.info(">>> " + getClass().getSimpleName() + " BEGIN");

        ResteasyClient client = new ResteasyClientBuilder().build();
        ResteasyWebTarget target = client.target(url);

        // get a resource to call
        CityResource resource = target.proxy(CityResource.class);

        // get city with integer type id
        CityIdIntegerDTO cityIdIntegerDTO = resource.createModelWithID();
        log.info(ToStringBuilder.reflectionToString(cityIdIntegerDTO));

        // get city with long type id
        CityIdLongDTO cityIdLongDTO = resource.createModelWithIDNoAudit();
        log.info(ToStringBuilder.reflectionToString(cityIdLongDTO));

        log.info("<<< " + getClass().getSimpleName() + " END");
    }
}
