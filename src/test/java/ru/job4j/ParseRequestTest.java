package ru.job4j;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.is;
import ru.job4j.PoohMQ.Broker.Request;

public class ParseRequestTest {

    @Test
    public void test() {
        String request = "POST /topic/weather/ {\"queue\" : \"weather\",\"text\" : \"temperature +18 C\"}";
        Request parseRequest = new Request(request);
        String typeOfRequest = parseRequest.getTypeOfRequest();
        String typeOfQueue = parseRequest.getTypeOfQueue();
        String theme = parseRequest.getTheme();
        String body = parseRequest.getBody();
        assertThat(typeOfRequest, is("POST"));
        assertThat(typeOfQueue, is("topic"));
        assertThat(theme, is("weather"));
        assertThat(body, is("{\"queue\" : \"weather\",\"text\" : \"temperature +18 C\"}"));
    }
}
