package com.coeus.api.config;

public interface TestConfigs {

    int SERVER_PORT = 8888;

    String HEADER_PARAM_AUTHORIZATION = "Authorization";
    String HEADER_PARAM_ORIGIN = "Origin";


    String AUTHORIZED_ORIGIN = "http://localhost:3000";
    String UNATHOURIZED_ORIGIN = "http://localhost:1234";

}
