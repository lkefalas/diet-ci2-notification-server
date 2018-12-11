package com.example.dietci2notificationserver;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.dietci2notificationserver.FcmClient.FcmClient;

@Service
@EnableScheduling
public class PushChuckJokeService {

	public static final Logger logger = LoggerFactory.getLogger("PushChuckJokeService");
	private final FcmClient fcmClient;

	private final WebClient webClient;

	private int seq = 0;

	public PushChuckJokeService(FcmClient fcmClient, WebClient webClient) {
		this.fcmClient = fcmClient;
		this.webClient = webClient;
		
		PushChuckJokeService.logger.info("Initing the service?");
	}

	@Scheduled(fixedDelay = 30_000)
	public void sendChuckQuotes() {
		
		PushChuckJokeService.logger.info("Sending the joke");
		IcndbJoke joke = this.webClient.get().uri("http://api.icndb.com/jokes/random")
				.retrieve().bodyToMono(IcndbJoke.class).block();
		try {
			sendPushMessage(joke);
		}
		catch (InterruptedException | ExecutionException e) {
			DietCi2NotificationServerApplication.logger.error("send chuck joke", e);
		}
		
		
	}

	void sendPushMessage(IcndbJoke joke) throws InterruptedException, ExecutionException {
		Map<String, String> data = new HashMap<>();
		data.put("id", String.valueOf(joke.getValue().getId()));
		data.put("joke", joke.getValue().getJoke());
		data.put("seq", String.valueOf(this.seq++));
		data.put("ts", String.valueOf(System.currentTimeMillis()));

		PushChuckJokeService.logger.info("Sending chuck joke...: " + data);
		this.fcmClient.send(data);
	}
}
