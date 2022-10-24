package data;

import dto.Percentage;

public class ItemData {

	private String itemId;
	private PricePoint estimatedPrice = new PricePoint();
	private int[] recentTradeValues;
	private int totalTrades;
	private Percentage itemValueCertaintyPercentage;
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public PricePoint getEstimatedPrice() {
		return estimatedPrice;
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
