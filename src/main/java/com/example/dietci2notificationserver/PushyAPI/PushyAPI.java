package com.example.dietci2notificationserver.PushyAPI;

import com.example.dietci2notificationserver.PushServiceSettings;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import java.util.Map;

import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.json.BasicJsonParser;
import org.springframework.boot.json.JsonParser;
import org.springframework.stereotype.Service;
import org.apache.http.entity.ByteArrayEntity;

@Service
public class PushyAPI {
	public static ObjectMapper mapper = new ObjectMapper();
	public final Logger log = LoggerFactory.getLogger(getClass());
	// Insert your Secret API Key here
	private String SECRET_API_KEY;
	private String pushyURL;
	
	public PushyAPI(PushServiceSettings settings) {
		this.SECRET_API_KEY = settings.getSecretApiKey();
		this.pushyURL = settings.getPushyURL();
		log.info("Settings are " + this.SECRET_API_KEY  +" " + this.pushyURL);
	}

	public void sendPush(PushyPushRequest req) throws Exception {
		// Create POST request
		HttpPost request = new HttpPost(pushyURL + SECRET_API_KEY);

		request.addHeader("Content-Type", "application/json");

		byte[] json = mapper.writeValueAsBytes(req);

		// Send post data as byte array
		request.setEntity(new ByteArrayEntity(json));

		try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
			handleResponse (client.execute(request, new BasicHttpContext()));
		}

		log.info("Successfully sent push message");
	}

	private void handleResponse(HttpResponse res) throws Exception {
		String responseJSON = EntityUtils.toString(res.getEntity());
		JsonParser jparser = new BasicJsonParser();
		Map<String, Object> map = jparser.parseMap(responseJSON);
		if (map.containsKey("error")) {
			throw new Exception(map.get("error").toString());
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