package com.restful.client.example;


import com.fererlab.animal.dto.Zoo;
import com.fererlab.animal.restful.AnimalResource;
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

import javax.ws.rs.core.Response;

public class InheritanceClient implements Runnable {

    private Log log = LogFactory.getLog(InheritanceClient.class);

    private final String url;

    public InheritanceClient(String url) {
        this.url = url;
    }

    @Override
    public void run() {
        log.info(">>> " + getClass().getSimpleName() + " BEGIN");

        ResteasyJacksonProvider resteasyJacksonProvider = new ResteasyJacksonProvider();
        ObjectMapper mapper = new ObjectMapper();
        TypeResolverBuilder<?> typeResolver = new CustomTypeResolverBuilder();
        typeResolver.init(JsonTypeInfo.Id.CLASS, null);
        typeResolver.inclusion(JsonTypeInfo.As.PROPERTY);
        typeResolver.typeProperty("@CLASS");
        mapper.setDefaultTyping(typeResolver);
        resteasyJacksonProvider.setMapper(mapper);

        ResteasyClient client = new ResteasyClientBuilder().build();

        client.register(resteasyJacksonProvider);
        ResteasyWebTarget target = client.target(url);

        // get a resource to call
        AnimalResource resource = target.proxy(AnimalResource.class);

        // get response, this response has @CLASS
        Zoo zoo = resource.getZoo();
        log.info(ToStringBuilder.reflectionToString(zoo));

        // below two do not have @CLASS since they don't have @XmlRootElement
        // annotation at server side, JacksonJsonProvider chooses the ObjectMapper according to annotation

        // get response
        String hi = resource.hi();
        log.info(hi);
        // get response
        Response response = resource.list();
        log.info(response.getEntity());

    }
}
