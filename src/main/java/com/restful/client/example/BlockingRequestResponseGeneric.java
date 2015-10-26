package com.restful.client.example;

import com.fererlab.car.dto.CarDTO;
import com.fererlab.car.restful.CarResource;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.jsontype.TypeResolverBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.plugins.providers.jackson.ResteasyJacksonProvider;

/**
 * blocking request response example for generic model (generic at server side)
 */
public class BlockingRequestResponseGeneric implements Runnable {

    private Log log = LogFactory.getLog(BlockingRequestResponseGeneric.class);

    private final String url;

    public BlockingRequestResponseGeneric(String url) {
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


        ResteasyWebTarget target = client.target(url);
        // get a resource to call
        CarResource resource = target.proxy(CarResource.class);

        // create a car
        CarDTO createCarDTO = new CarDTO();
        createCarDTO.setManufacturer("SAAB");
        createCarDTO.setModel(1999);
        CarDTO newCarDTO = resource.create(createCarDTO);
        log.info(ToStringBuilder.reflectionToString(newCarDTO));

        // find the created car
        CarDTO foundCarDTO = resource.find(newCarDTO.getId());
        log.info(ToStringBuilder.reflectionToString(foundCarDTO));

        // update car
        foundCarDTO.setModel(2000);
        CarDTO updatedCarDTO = resource.update(foundCarDTO);
        log.info(ToStringBuilder.reflectionToString(updatedCarDTO));

        // delete car
        CarDTO deletedCarDTO = resource.delete(updatedCarDTO.getId());
        log.info(ToStringBuilder.reflectionToString(deletedCarDTO));

        log.info("<<< " + getClass().getSimpleName() + " END");
    }
}
