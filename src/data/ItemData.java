package data;

import dto.Percentage;

public class ItemData {

	private String itemId;
	private float estimatedMinimumPrice;
	private float estimatedMaximumPrice;
	private float estimatedMedianPrice;
	private int[] recentTradeValues;
	private int totalTrades;
	private Percentage itemValueCertaintyPercentage;
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public float getEstimatedMinimumPrice() {
		return estimatedMinimumPrice;
	}
	public void setEstimatedMinimumPrice(float estimatedMinimumPrice) {
		this.estimatedMinimumPrice = estimatedMinimumPrice;
	}
	public float getEstimatedMaximumPrice() {
		return estimatedMaximumPrice;
	}
	public void setEstimatedMaximumPrice(float estimatedMaximumPrice) {
		this.estimatedMaximumPrice = estimatedMaximumPrice;
	}
	public float getEstimatedMedianPrice() {
		return estimatedMedianPrice;
	}
	public void setEstimatedMedianPrice(float estimatedMedianPrice) {
		this.estimatedMedianPrice = estimatedMedianPrice;
	}
	public int[] getRecentTradeValues() {
		return recentTradeValues;
	}
	public void setRecentTradeValues(int[] recentTradeValues) {
		this.recentTradeValues = recentTradeValues;
	}
	public int getTotalTrades() {
		return totalTrades;
	}
	public void setTotalTrades(int totalTrades) {
		this.totalTrades = totalTrades;
	}
	public Percentage getItemValueCertaintyPercentage() {
		return itemValueCertaintyPercentage;
	}
	public void setItemValueCertaintyPercentage(Percentage itemValueCertaintyPercentage) {
		this.itemValueCertaintyPercentage = itemValueCertaintyPercentage;
	}
	
	public void incrementTotalTrades() {
		totalTrades++;
	}
	
}
