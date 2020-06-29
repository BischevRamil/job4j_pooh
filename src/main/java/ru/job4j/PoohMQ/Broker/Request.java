package ru.job4j.PoohMQ.Broker;

public class Request {
    //json
    private String request;
    //POST or GET
    private String typeOfRequest;
    //Queue or Topic
    private String typeOfQueue;
    //Theme
    private String theme;
    //Body
    private String body = "";

    public Request(String request) {
        this.request = request;
        parseRequest();
    }

    private void parseRequest() {
        this.typeOfRequest = this.request.split("/")[0].trim();
        this.typeOfQueue = this.request.split("/")[1].trim();
        this.theme = this.request.split("/")[2].trim();
        if (this.getTypeOfRequest().equalsIgnoreCase("POST")) {
            this.body = this.request.split("/")[3].trim();
        }
    }

    public String getTypeOfRequest() {
        return typeOfRequest;
    }

    public String getTypeOfQueue() {
        return typeOfQueue;
    }

    public String getTheme() {
        return theme;
    }

    public String getBody() {
        return body;
    }
}
