package com.restful.client;


import com.fererlab.restful.CityResource;
import com.fererlab.service.dto.CityIdIntegerDTO;
import com.fererlab.service.dto.CityIdLongDTO;
import org.apache.commons.lang.builder.ToStringBuilder;

public class Client {

    private String url = "http://localhost:8080/restful-server-city/api/";

    public static void main(String[] args) {
        Client client = new Client();
        client.call();
    }

    private void call() {
        // get a resource to call
        CityResource resource = Resources.create(CityResource.class, url);

        // get city with integer type id
        CityIdIntegerDTO cityIdIntegerDTO = resource.createModelWithID();
        System.out.println(ToStringBuilder.reflectionToString(cityIdIntegerDTO));

        // get city with long type id
        CityIdLongDTO cityIdLongDTO = resource.createModelWithIDNoAudit();
        System.out.println(ToStringBuilder.reflectionToString(cityIdLongDTO));
    }

}
