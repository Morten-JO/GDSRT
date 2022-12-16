package dto;

import util.DateStamper;

public class TradeResult {

	public enum TradeCalculated {
		COMPLETED, ERROR, NOT_COMPLETED, INCONCLUSIVE, FINALIZED
	}
	
	private TradeCalculated tradeCalculated = TradeCalculated.NOT_COMPLETED;
	private int tradeMedianValueDifference;
	private int tradeMinimumValueDifference;
	private int tradeMaximumValueDifference;
	private int tradeWarningLevel;
	private String timeStampCalculated;
	private String checksum;
	
	public TradeResult() {
		
	}
	
	public TradeResult(TradeCalculated res, int median, int minimum, int maximum, int level, String checksum) {
		this.tradeCalculated = res;
		this.tradeMedianValueDifference = median;
		this.tradeMinimumValueDifference = minimum;
		this.tradeMaximumValueDifference = maximum;
		this.tradeWarningLevel = level;
		this.timeStampCalculated = DateStamper.getStampedDate();
		this.checksum = checksum;
	}
	
	public TradeResult(TradeCalculated res, float median, float minimum, float maximum, int level, String checksum) {
		this.tradeCalculated = res;
		this.tradeMedianValueDifference = (int)median;
		this.tradeMinimumValueDifference = (int)minimum;
		this.tradeMaximumValueDifference = (int)maximum;
		this.tradeWarningLevel = level;
		this.timeStampCalculated = DateStamper.getStampedDate();
		this.checksum = checksum;
	}
	
	public TradeCalculated getTradeCalculated() {
		return tradeCalculated;
	}
	public void setTradeCalculated(TradeCalculated tradeCalculated) {
		this.tradeCalculated = tradeCalculated;
	}
	public int getTradeMedianValueDifference() {
		return tradeMedianValueDifference;
	}
	public void setTradeMedianValueDifference(int tradeMedianValueDifference) {
		this.tradeMedianValueDifference = tradeMedianValueDifference;
	}
	public int getTradeMinimumValueDifference() {
		return tradeMinimumValueDifference;
	}
	public void setTradeMinimumValueDifference(int tradeMinimumValueDifference) {
		this.tradeMinimumValueDifference = tradeMinimumValueDifference;
	}
	public int getTradeMaximumValueDifference() {
		return tradeMaximumValueDifference;
	}
	public void setTradeMaximumValueDifference(int tradeMaximumValueDifference) {
		this.tradeMaximumValueDifference = tradeMaximumValueDifference;
	}
	public int getTradeWarningLevel() {
		return tradeWarningLevel;
	}
	public void setTradeWarningLevel(int tradeWarningLevel) {
		this.tradeWarningLevel = tradeWarningLevel;
	}
	public String getTimeStampCalculated() {
		return timeStampCalculated;
	}
	public void setTimeStampCalculated(String timeStampCalculated) {
		this.timeStampCalculated = timeStampCalculated;
	}
	public String getChecksum() {
		return checksum;
	}
	public void setChecksum(String checksum) {
		this.checksum = checksum;
	}
	
	
	
	
	
	
}
