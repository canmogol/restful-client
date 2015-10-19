package com.restful.client;


import com.restful.client.example.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client {

    ExecutorService pool = Executors.newCachedThreadPool();

    private String url = "http://localhost:8080/restful-server/api/";

    public static void main(String[] args) {
        Client client = new Client();
        client.runExamples();
    }

    private void runExamples() {
        pool.execute(new MultipleEntityType(url));
        pool.execute(new BlockingRequestResponse(url));
        pool.execute(new BlockingRequestResponseGeneric(url));
        pool.execute(new AsyncServerBlockingClient(url));
        pool.execute(new AsyncServerNonBlockingClient(url));
    }

}
