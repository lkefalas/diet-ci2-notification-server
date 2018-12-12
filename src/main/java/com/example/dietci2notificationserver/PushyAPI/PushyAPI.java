package com.example.dietci2notificationserver.PushyAPI;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import java.io.IOException;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.util.EntityUtils;
import org.apache.http.entity.ByteArrayEntity;

import java.util.Map;

public class PushyAPI {
    public static ObjectMapper mapper = new ObjectMapper();
    
    // Insert your Secret API Key here
    public static final String SECRET_API_KEY = "5cfc9c92b6caaa0ea17856334e211d26047374428f92ad96f5ff7d06c3127c6e";

    public static void sendPush(PushyPushRequest req) throws Exception {
        // Create POST request
        HttpPost request = new HttpPost("https://api.pushy.me/push?api_key=" + SECRET_API_KEY);

        // Set content type to JSON
        request.addHeader("Content-Type", "application/json");

        // Convert post data to JSON
        byte[] json = mapper.writeValueAsBytes(req);

        // Send post data as byte array
        request.setEntity(new ByteArrayEntity(json));

        // Execute the request
        
        try (CloseableHttpClient client = HttpClientBuilder.create().build()) {

        	HttpResponse response = client.execute(request, new BasicHttpContext());
        	String responseJSON = EntityUtils.toString(response.getEntity());
        	Map<String, Object> map = mapper.readValue(responseJSON, Map.class);
        	// Got an error?
            if (map.containsKey("error")) {
                // Throw it
                throw new Exception(map.get("error").toString());
            }

        } catch (IOException e) {
			System.out.println(e);
        }
    }

    public static class PushyPushRequest {
        public Object to;
        public Object data;

        public Object notification;

        public PushyPushRequest(Object data, Object to, Object notification) {
            this.to = to;
            this.data = data;
            this.notification = notification;
        }
    }
}