package com.restful.client.example;

import com.fererlab.core.exception.ServerError;
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
import com.fererlab.user.exception.*;

import javax.ws.rs.WebApplicationException;

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

        try {
            // throw some exceptions :)
            UserDTO userDTO1 = resource.throwUserDatabase(1);
            UserDTO userDTO2 = resource.throwUser(2);
            UserDTO userDTO3 = resource.throwUserIO(3);
            UserDTO userDTO4 = resource.throwNullPointer(4);

            // create a user
            UserDTO createUserDTO = new UserDTO();
            createUserDTO.setUsername("john");
            createUserDTO.setPassword("123");
            UserDTO newUserDTO = resource.create(createUserDTO);
            log.info(ToStringBuilder.reflectionToString(newUserDTO));

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

        } catch (WebApplicationException e) {
            ServerError serverError = new ServerError(e);
            Exception exception = serverError.getRootException();
            if (exception instanceof UserException) {
                log.info("Exception Occurred >>> UserException: " + exception);
            } else if (exception instanceof UserDatabaseException) {
                log.info("Exception Occurred >>> UserDatabaseException: " + exception);
            } else if (exception instanceof UserIOException) {
                log.info("Exception Occurred >>> UserIOException: " + exception);
            } else if (exception instanceof NullPointerException) {
                log.info("Exception Occurred >>> NullPointerException: " + exception);
            } else if (exception instanceof RuntimeException) {
                log.info("Exception Occurred >>> RuntimeException: " + exception);
            }
            log.info(serverError);
        } catch (Exception e) {
            log.info("Exception Occurred >>> e = " + e);
        }

        log.info("<<< " + getClass().getSimpleName() + " END");
    }
}
