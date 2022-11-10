package data;

import java.util.List;

import dto.Trade;

public class UserTradeGraph {

	public enum DetailLevel{
		ALL,MEDIUMWARNING,HIGHWARNING
	}
	
	private UserTradeGraph owner;
	private List<Trade> tradesWithOwner;
	private String userId;
	private int layers;
	private List<UserTradeGraph> points;
	private List<Trade> trades;
	private int warningLevel;
	
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public int getLayers() {
		return layers;
	}
	public void setLayers(int layers) {
		this.layers = layers;
	}
	public List<UserTradeGraph> getPoints() {
		return points;
	}
	public void setPoints(List<UserTradeGraph> points) {
		this.points = points;
	}
	public List<Trade> getTrades() {
		return trades;
	}
	public void setTrades(List<Trade> trades) {
		this.trades = trades;
	}
	public int getWarningLevel() {
		return warningLevel;
	}
	public void setWarningLevel(int warningLevel) {
		this.warningLevel = warningLevel;
	}
	public UserTradeGraph getOwner() {
		return owner;
	}
	public void setOwner(UserTradeGraph owner) {
		this.owner = owner;
	}
	public List<Trade> getTradesWithOwner() {
		return tradesWithOwner;
	}
	public void setTradesWithOwner(List<Trade> tradesWithOwner) {
		this.tradesWithOwner = tradesWithOwner;
	}
}
