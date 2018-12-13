package com.example.dietci2notificationserver;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "push")
@Component
public class PushServiceSettings {
	private String serviceAccountFile;
	private String secretApiKey;
	private String pushyURL;

	public String getServiceAccountFile() {
		return this.serviceAccountFile;
	}

	public void setServiceAccountFile(String serviceAccountFile) {
		this.serviceAccountFile = serviceAccountFile;
	}

	public String getSecretApiKey() {
		return secretApiKey;
	}

	public void setSecretApiKey(String secretApiKey) {
		this.secretApiKey = secretApiKey;
	}

	public String getPushyURL() {
		return pushyURL;
	}

	public void setPushyURL(String pushyURL) {
		this.pushyURL = pushyURL;
	}

}