package com.restful.client;


import com.fererlab.async.dto.AsyncRequestDTO;
import com.fererlab.async.dto.AsyncResponseDTO;
import com.fererlab.car.dto.CarDTO;
import com.fererlab.car.restful.CarResource;
import com.fererlab.city.dto.CityIdIntegerDTO;
import com.fererlab.city.dto.CityIdLongDTO;
import com.fererlab.city.restful.CityResource;
import com.fererlab.user.dto.UserDTO;
import com.fererlab.user.restful.UserResource;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Client {

    private String url = "http://localhost:8080/restful-server/api/";

    public static void main(String[] args) {
        Client client = new Client();
        client.runMultipleEntityTypeExample();
        client.runBlockingRequestResponseExample();
        client.runBlockingRequestResponseGenericExample();
        client.runAsyncServerBlockingClient();
        client.runAsyncServerNonBlockingClient();
    }

    private void runMultipleEntityTypeExample() {
        ResteasyClient client = new ResteasyClientBuilder().build();
        ResteasyWebTarget target = client.target(url);

        // get a resource to call
        CityResource resource = target.proxy(CityResource.class);

        // get city with integer type id
        CityIdIntegerDTO cityIdIntegerDTO = resource.createModelWithID();
        System.out.println(ToStringBuilder.reflectionToString(cityIdIntegerDTO));

        // get city with long type id
        CityIdLongDTO cityIdLongDTO = resource.createModelWithIDNoAudit();
        System.out.println(ToStringBuilder.reflectionToString(cityIdLongDTO));
    }

    private void runBlockingRequestResponseExample() {
        ResteasyClient client = new ResteasyClientBuilder().build();
        ResteasyWebTarget target = client.target(url);
        // get a resource to call
        UserResource resource = target.proxy(UserResource.class);

        // create a user
        UserDTO createUserDTO = new UserDTO();
        createUserDTO.setUsername("john");
        createUserDTO.setPassword("123");
        UserDTO newUserDTO = resource.create(createUserDTO);
        System.out.println(ToStringBuilder.reflectionToString(newUserDTO));

        // find the created user
        UserDTO foundUserDTO = resource.find(newUserDTO.getId());
        System.out.println(ToStringBuilder.reflectionToString(foundUserDTO));

        // update user
        foundUserDTO.setUsername("Mike");
        UserDTO updatedUserDTO = resource.update(foundUserDTO);
        System.out.println(ToStringBuilder.reflectionToString(updatedUserDTO));

        // delete user
        UserDTO deletedUserDTO = resource.delete(updatedUserDTO.getId());
        System.out.println(ToStringBuilder.reflectionToString(deletedUserDTO));

    }

    private void runBlockingRequestResponseGenericExample() {
        ResteasyClient client = new ResteasyClientBuilder().build();
        ResteasyWebTarget target = client.target(url);
        // get a resource to call
        CarResource resource = target.proxy(CarResource.class);

        // create a car
        CarDTO createCarDTO = new CarDTO();
        createCarDTO.setManufacturer("SAAB");
        createCarDTO.setModel(1999);
        CarDTO newCarDTO = resource.create(createCarDTO);
        System.out.println(ToStringBuilder.reflectionToString(newCarDTO));

        // find the created car
        CarDTO foundCarDTO = resource.find(newCarDTO.getId());
        System.out.println(ToStringBuilder.reflectionToString(foundCarDTO));

        // update car
        foundCarDTO.setModel(2000);
        CarDTO updatedCarDTO = resource.update(foundCarDTO);
        System.out.println(ToStringBuilder.reflectionToString(updatedCarDTO));

        // delete car
        CarDTO deletedCarDTO = resource.delete(updatedCarDTO.getId());
        System.out.println(ToStringBuilder.reflectionToString(deletedCarDTO));
    }

    private void runAsyncServerBlockingClient() {
        WebTarget target = ClientBuilder.newClient().target(url);
        final AsyncInvoker asyncInvoker = target.path("/async/executorRunnable").request().async();
        Entity entity = Entity.entity("John", MediaType.WILDCARD_TYPE);
        final Future<Response> responseFuture = asyncInvoker.post(entity);
        System.out.println("Async server, blocking client, begin");
        try {
            Response response = responseFuture.get();
            responseFuture.get(10, TimeUnit.SECONDS); // block for max 10 seconds
            System.out.println("got response: " + response);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            System.out.println("Exception: " + e.getMessage());
        }
        System.out.println("Async server, blocking client, end");
    }

    private void runAsyncServerNonBlockingClient() {
        ResteasyClient client = new ResteasyClientBuilder().build();
        ResteasyWebTarget target = client.target(url).path("/async/sayHi");
        target.queryParam("name", "john");
        AsyncRequestDTO asyncRequestDTO = new AsyncRequestDTO();
        asyncRequestDTO.setRequest("john");
        Entity entity = Entity.entity(asyncRequestDTO, MediaType.APPLICATION_JSON);
        target.request().async().post(
                entity,
                new InvocationCallback<AsyncResponseDTO>() {
                    @Override
                    public void completed(AsyncResponseDTO response) {
                        System.out.println(response.getResponse());
                    }

                    @Override
                    public void failed(Throwable throwable) {
                        System.out.println("Exception: " + throwable.getMessage());
                    }
                });
    }

}
