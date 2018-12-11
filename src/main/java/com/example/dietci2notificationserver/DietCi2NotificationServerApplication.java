package com.example.dietci2notificationserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class DietCi2NotificationServerApplication {
	
	public static final Logger logger = LoggerFactory.getLogger("DietCi2NotificationServerApplication");

	public static void main(String[] args) {
		SpringApplication.run(DietCi2NotificationServerApplication.class, args);
	}
	
	@Bean
	public WebClient webClient() {
		return WebClient.create();
	}
}