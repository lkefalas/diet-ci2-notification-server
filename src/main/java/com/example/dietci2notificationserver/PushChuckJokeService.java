package com.example.dietci2notificationserver;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;

import com.example.dietci2notificationserver.FcmClient.FcmClient;

@Service
@EnableScheduling
public class PushChuckJokeService {

	public final Logger log = LoggerFactory.getLogger(getClass());
	private final FcmClient fcmClient;

//	private final WebClient webClient;

	private int seq = 0;

	public PushChuckJokeService(FcmClient fcmClient) {
		this.fcmClient = fcmClient;
//		this.webClient = webClient;
		
		log.info("Initializing the service");
	}

//	@Scheduled(fixedDelay = 5_000)
	public void sendChuckQuotes() {
		try {
			sendPushMessage(new DietitianMessage());
		}
		catch (InterruptedException | ExecutionException e) {
			log.error("Error pushing scheduled message", e);
		}
		
	}

	void sendPushMessage(DietitianMessage msg) throws InterruptedException, ExecutionException {
		Map<String, String> data = new HashMap<>();
		data.put("id", String.valueOf(msg.getDietitianId()));
		data.put("msg", msg.getMessage());
		data.put("seq", String.valueOf(this.seq++));
		data.put("ts", String.valueOf(System.currentTimeMillis()));

		log.info("Sending push message: " + data);
		this.fcmClient.send(data);
	}
}
