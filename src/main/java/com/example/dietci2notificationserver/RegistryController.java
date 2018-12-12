package com.example.dietci2notificationserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.dietci2notificationserver.FcmClient.FcmClient;
import com.example.dietci2notificationserver.PushyAPI.PushyAPI;

import reactor.core.publisher.Mono;

@RestController
@CrossOrigin
public class RegistryController {

	public final Logger log = LoggerFactory.getLogger(getClass());
	private final FcmClient fcmClient;
	
	// Prepare list of target device tokens
	List<String> deviceTokens = new ArrayList<String>();

	public RegistryController(FcmClient fcmClient) {
		this.fcmClient = fcmClient;
	}

	@PostMapping("/register")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public Mono<Void> register( @RequestBody Mono<String> token) {
		return token.doOnNext(t -> this.fcmClient.subscribe("chuck", t)).then();
	}
	
	@RequestMapping("/blabla")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void registerPushyDevice() throws InterruptedException, ExecutionException {
		log.info("Send sample push");
		sendSamplePush();
	}
	
	@RequestMapping(value="/device", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void blabla(@RequestParam("token") String deviceid) throws InterruptedException, ExecutionException {
		log.info("received notification from: " + deviceid);
		
		// Add your device tokens here
		this.deviceTokens.add(deviceid);
	}
	
	public void sendSamplePush() {
		// Convert to String[] array
		String[] topic = deviceTokens.toArray(new String[deviceTokens.size()]);

		// Optionally, send to a publish/subscribe topic instead
		// String to = '/topics/news';

		// Set payload (any object, it will be serialized to JSON)
		Map<String, String> payload = new HashMap<>();

		// Add "message" parameter to payload
		payload.put("message", "Hello World!");

		// iOS notification fields
		Map<String, Object> notification = new HashMap<>();

		notification.put("badge", 1);
		notification.put("sound", "ping.aiff");
		notification.put("body", "Hello World \u270c");

		// Prepare the push request
		PushyAPI.PushyPushRequest push = new PushyAPI.PushyPushRequest(payload, topic, notification);

		try {
			// Try sending the push notification
			PushyAPI.sendPush(push);
		}
		catch (Exception exc) {
			log.error("Sending push message failed: " + exc);
		}
		
		log.info("sdfsdfsdf");
	}
}