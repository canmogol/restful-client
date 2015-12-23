package com.restful.client;


import com.restful.client.example.BlockingRequestResponse;

public class Client {

    private String restfulWebServiceURL = "http://localhost:8080/restful-server/api/";
    private String websocketURL = "ws://localhost:8080/restful-server/";

    public static void main(String[] args) {
        Client client = new Client();
        client.runExamples();
    }

    private void runExamples() {

//        // BELOW IS RESTFUL WEB IMAGE UPLOAD DOWNLOAD EXAMPLE
//
//        // blocking file upload download rws
//        new BlockingImageUploadDownload(restfulWebServiceURL).run();
//
//
//
//        // BELOW ARE RESTFUL WEB SERVICE EXAMPLES (RWS)
//
//        // multiple entity types with different method signatures rws
//        new MultipleEntityType(restfulWebServiceURL).run();
//
//        // single entity blocking rws
        new BlockingRequestResponse(restfulWebServiceURL).run();
//
//        // generic impl for rws
//        new BlockingRequestResponseGeneric(restfulWebServiceURL).run();
//
//        // async server for blocking client rws
//        new AsyncServerBlockingClient(restfulWebServiceURL).run();
//
//        // async server and non-blocking client rws
//        new AsyncServerNonBlockingClient(restfulWebServiceURL).run();
//
//        // multiple inheritance client rws
//        new InheritanceClient(restfulWebServiceURL).run();
//
//
//
//        // BELOW ARE WEB SOCKET EXAMPLES (WS)
//
//        // single web socket client
//        new WebSocketSingleClient(websocketURL).run();
//
//        // below is the test for 3 subscribers and 1 publisher, start order is not important
//        new WebSocketSubscriber(websocketURL).run();
//        new WebSocketPublisher(websocketURL).run();
//        new WebSocketSubscriber(websocketURL).run();
//        new WebSocketSubscriber(websocketURL).run();
//
//        // below are web socket sample clients
//        new WebSocketStreamPartByPartClient(websocketURL).run();
//        new WebSocketStreamWholeMessageClient(websocketURL).run();
//        // using device simulator
//        new WebSocketStreamDevicePartByPartClient(websocketURL).run();
//        new WebSocketStreamDeviceWholeMessageClient(websocketURL).run();

    }

}
