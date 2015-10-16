package com.restful.client;

import com.restful.city.model.CityIDAudited;
import com.restful.city.model.CityOIDAudited;
import com.restful.city.model.CityUUIDAudited;
import com.restful.restful.CityResource;
import org.apache.commons.lang.builder.ToStringBuilder;

public class Client {

    private String url = "http://localhost:8080/restful-server-city/api/";
    private CityResource resource = Resources.create(CityResource.class, url);

    public static void main(String[] args) {
        Client client = new Client();
        client.call();
    }

    private void call() {
        CityIDAudited cityID = resource.createModelWithID();
        System.out.println(ToStringBuilder.reflectionToString(cityID));

        CityIDAudited cityID2 = resource.findModelWithID(cityID.getId().intValue());
        System.out.println(ToStringBuilder.reflectionToString(cityID2));

        CityUUIDAudited cityUUID = resource.createModelWithUUID();
        System.out.println(ToStringBuilder.reflectionToString(cityUUID));

        CityOIDAudited cityOID = resource.createModelWithOID();
        System.out.println(ToStringBuilder.reflectionToString(cityOID));

    }

}
