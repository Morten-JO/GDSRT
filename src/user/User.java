package user;

public class User {

	private String userIdentification;
	private int currentAggroLevel;
	
	public User(String userIdentification, int currentAggroLevel) {
		this.userIdentification = userIdentification;
		this.currentAggroLevel = currentAggroLevel;
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
	
	
	
}
