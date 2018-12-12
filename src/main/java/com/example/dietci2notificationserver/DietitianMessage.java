package com.example.dietci2notificationserver;

public class DietitianMessage {
	private String message;
	private double dietitianId;
	
	DietitianMessage() {
		this.message = "kokokoko";
		this.dietitianId = Math.random();
	}
	public String getMessage() {
		return this.message;
	}
	public double getDietitianId() {
		return dietitianId;
	}
}
