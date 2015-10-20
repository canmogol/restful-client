package com.restful.client;


import com.restful.client.example.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client {

    ExecutorService pool = Executors.newCachedThreadPool();

    private String restfulWebServiceURL = "http://localhost:8080/restful-server/api/";
    private String websocketURL = "ws://localhost:8080/restful-server/";

    public static void main(String[] args) {
        Client client = new Client();
        client.runExamples();
    }

    private void runExamples() {

        // BELOW ARE RESTFUL WEB SERVICE EXAMPLES (RWS)

        // multiple entity types with different method signatures rws
        pool.execute(new MultipleEntityType(restfulWebServiceURL));

        // single entity blocking rws
        pool.execute(new BlockingRequestResponse(restfulWebServiceURL));

        // generic impl for rws
        pool.execute(new BlockingRequestResponseGeneric(restfulWebServiceURL));

        // async server for blocking client rws
        pool.execute(new AsyncServerBlockingClient(restfulWebServiceURL));

        // async server and non-blocking client rws
        pool.execute(new AsyncServerNonBlockingClient(restfulWebServiceURL));

        // multiple inheritance client rws
        pool.execute(new InheritanceClient(restfulWebServiceURL));

        // BELOW ARE WEB SOCKET EXAMPLES (WS)

        // single web socket client
        pool.execute(new WebSocketSingleClient(websocketURL));

        // below is the test for 3 subscribers and 1 publisher, start order is not important
        pool.execute(new WebSocketSubscriber(websocketURL));
        pool.execute(new WebSocketPublisher(websocketURL));
        pool.execute(new WebSocketSubscriber(websocketURL));
        pool.execute(new WebSocketSubscriber(websocketURL));

        pool.execute(new WebSocketStreamClient(websocketURL));

    }

}
