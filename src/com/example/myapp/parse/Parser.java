package com.example.myapp.parse;

import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;

import java.io.IOException;

public class Parser {
    static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    static final JsonFactory JSON_FACTORY = new JacksonFactory();
    private static final Parser instance = new Parser();

    private Parser() {
    }

    HttpRequestFactory requestFactory =
            HTTP_TRANSPORT.createRequestFactory(new HttpRequestInitializer() {
                @Override
                public void initialize(HttpRequest request) {
                    request.setParser(new JsonObjectParser(JSON_FACTORY));
                }
            });

    public ResponseDataWrapper parse(String inUrl) throws IOException {

        GenericUrl url = new GenericUrl(inUrl);
        HttpRequest request = null;
        try {
            request = requestFactory.buildGetRequest(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return parseResponse(request.execute());


    }

    private ResponseDataWrapper parseResponse(HttpResponse response) throws IOException {
        return response.parseAs(ResponseDataWrapper.class);
    }

    public static Parser getInstance() {
        return instance;
    }

}
