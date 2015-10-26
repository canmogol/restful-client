package com.restful.client.example;


import com.fererlab.city.restful.CityResource;
import com.fererlab.city.serviceengine.dto.CityIdIntegerDTO;
import com.fererlab.city.serviceengine.dto.CityIdLongDTO;
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
