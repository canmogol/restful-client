package com.restful.client;


import com.fererlab.city.dto.CityIdIntegerDTO;
import com.fererlab.city.dto.CityIdLongDTO;
import com.fererlab.city.restful.CityResource;
import com.fererlab.user.dto.UserDTO;
import com.fererlab.user.restful.UserResource;
import org.apache.commons.lang.builder.ToStringBuilder;

public class Client {

    private String url = "http://localhost:8080/restful-server/api/";

    public static void main(String[] args) {
        Client client = new Client();
        client.runMultipleEntityTypeExample();
        client.runBlockingRequestResponseExample();
    }

    private void runMultipleEntityTypeExample() {
        // get a resource to call
        CityResource resource = Resources.create(CityResource.class, url);

        // get city with integer type id
        CityIdIntegerDTO cityIdIntegerDTO = resource.createModelWithID();
        System.out.println(ToStringBuilder.reflectionToString(cityIdIntegerDTO));

        // get city with long type id
        CityIdLongDTO cityIdLongDTO = resource.createModelWithIDNoAudit();
        System.out.println(ToStringBuilder.reflectionToString(cityIdLongDTO));
    }

    private void runBlockingRequestResponseExample() {
        // get a resource to call
        UserResource resource = Resources.create(UserResource.class, url);

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

}
