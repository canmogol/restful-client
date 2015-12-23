package com.restful.client.example;

import com.fererlab.core.exception.ServiceException;
import com.fererlab.user.restful.UserResource;
import com.fererlab.user.serviceengine.dto.UserDTO;
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

import javax.ws.rs.InternalServerErrorException;

/**
 * blocking request and response example for single model
 */
public class BlockingRequestResponse implements Runnable {

    private Log log = LogFactory.getLog(BlockingRequestResponse.class);

    private final String url;

    public BlockingRequestResponse(String url) {
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
        UserResource resource = target.proxy(UserResource.class);

        // create a user
        UserDTO createUserDTO = new UserDTO();
        createUserDTO.setUsername("john");
        createUserDTO.setPassword("123");
        UserDTO newUserDTO = null;
        try {
            newUserDTO = resource.create(createUserDTO);
            log.info(ToStringBuilder.reflectionToString(newUserDTO));
        } catch (InternalServerErrorException e) {
            ServiceException serverErrorException = new ServiceException(e);
            switch (serverErrorException.getErrorCode()) {
                case DB:
                    System.out.println("database error: " + serverErrorException.getMessage());
                    break;
                case WS:
                    System.out.println("web service error: " + serverErrorException.getMessage());
                    break;
                case AUTHENTICATION:
                    System.out.println("authentication error: " + serverErrorException.getMessage());
                    break;
                default:
                    System.out.println("error: " + serverErrorException.getMessage());
                    break;
            }
        }

        // find the created user
        UserDTO foundUserDTO = resource.find(newUserDTO.getId());
        log.info(ToStringBuilder.reflectionToString(foundUserDTO));

        // update user
        foundUserDTO.setUsername("Mike");
        UserDTO updatedUserDTO = resource.update(foundUserDTO);
        log.info(ToStringBuilder.reflectionToString(updatedUserDTO));

        // delete user
        UserDTO deletedUserDTO = resource.delete(updatedUserDTO.getId());
        log.info(ToStringBuilder.reflectionToString(deletedUserDTO));

        log.info("<<< " + getClass().getSimpleName() + " END");
    }
}
