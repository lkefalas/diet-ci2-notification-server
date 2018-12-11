package com.example.dietci2notificationserver.FcmClient;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.TopicManagementResponse;
import com.google.firebase.messaging.WebpushConfig;
import com.google.firebase.messaging.WebpushNotification;

import com.example.dietci2notificationserver.FcmSettings;

@Service
public class FcmClient {

	public static final Logger logger = LoggerFactory.getLogger("FcmClient");
	public FcmClient(FcmSettings settings) {
		Path p = Paths.get(settings.getServiceAccountFile());
		try (InputStream serviceAccount = Files.newInputStream(p)) {
			FirebaseOptions options = new FirebaseOptions.Builder()
					.setCredentials(GoogleCredentials.fromStream(serviceAccount)).build();

			FirebaseApp.initializeApp(options);
			FcmClient.logger.info("Init fcm with options: " + options.toString() );
		}
		catch (IOException e) {
			FcmClient.logger.error("Error initializing fcm", e);
		}
	}

	public void send(Map<String, String> data)
			throws InterruptedException, ExecutionException {

		Message message = Message.builder().putAllData(data).setTopic("chuck")
				.setWebpushConfig(WebpushConfig.builder().putHeader("ttl", "1000")
						.setNotification(new WebpushNotification("Title of notification",
								data.toString(), "mail.png"))
						.build())
				.build();

		FirebaseMessaging.getInstance().sendAsync(message).get();
	}

	public void subscribe(String topic, String clientToken) {
		try {
			TopicManagementResponse response = FirebaseMessaging.getInstance()
					.subscribeToTopicAsync(Collections.singletonList(clientToken), topic).get();

			FcmClient.logger.info("Tokens that were subscribed successfully: "+
					new Integer(response.getSuccessCount()).toString());
		}
		catch (InterruptedException | ExecutionException e) {
			FcmClient.logger.error("subscribe", e);
		}
	}
}
