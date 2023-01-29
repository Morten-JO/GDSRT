package user;

import java.util.Map;

import util.AdditionalDataConverter;

public class User {

	private String userIdentification;
	private int currentAggroLevel;
	private Map<String, String> additionalData;

	public User(String userIdentification, int currentAggroLevel, String additionalData) {
		this.userIdentification = userIdentification;
		this.currentAggroLevel = currentAggroLevel;
		this.additionalData = AdditionalDataConverter.stringToMap(additionalData);
	}
	public String getUserIdentification() {
		return userIdentification;
	}
	public void setUserIdentification(String userIdentification) {
		this.userIdentification = userIdentification;
	}
	public int getCurrentAggroLevel() {
		return currentAggroLevel;
	}
	public void setCurrentAggroLevel(int currentAggroLevel) {
		this.currentAggroLevel = currentAggroLevel;
	}
	public Map<String, String> getAdditionalData() {
		return additionalData;
	}
	public void setAdditionalData(Map<String, String> additionalData) {
		this.additionalData = additionalData;
	}
}
