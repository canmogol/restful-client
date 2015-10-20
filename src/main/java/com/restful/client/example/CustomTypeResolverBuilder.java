package com.restful.client.example;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;

public class CustomTypeResolverBuilder extends ObjectMapper.DefaultTypeResolverBuilder {
    public CustomTypeResolverBuilder() {
        super(ObjectMapper.DefaultTyping.NON_FINAL);
    }

    @Override
    public boolean useForType(JavaType t) {
        return true;
    }
}