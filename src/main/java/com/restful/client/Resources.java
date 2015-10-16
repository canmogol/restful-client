package com.restful.client;

import org.apache.cxf.jaxrs.client.JAXRSClientFactory;

import java.util.Collections;

public class Resources<T> {

    private static Resources instance = null;

    private Resources() {
    }

    @SuppressWarnings("unchecked")
    public static <T> T create(Class<T> t, String url) {
        return (T) getInstance().createResource(t, url);
    }

    private T createResource(Class<T> t, String url) {
        T instance = JAXRSClientFactory.create(url, t, Collections.singletonList(new org.codehaus.jackson.jaxrs.JacksonJsonProvider()));
        return instance;
    }

    private static Resources getInstance() {
        if (instance == null) {
            instance = new Resources();
        }
        return instance;
    }

}
