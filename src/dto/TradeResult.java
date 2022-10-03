package dto;

public class TradeResult {

	public enum TradeCalculated {
		COMPLETED, ERROR, NOT_COMPLETED
	}
	
	private TradeCalculated tradeCalculated = TradeCalculated.NOT_COMPLETED;
	private int tradeMedianValueDifference;
	private int tradeMinimumValueDifference;
	private int tradeMaximumValueDifference;
	private int tradeWarningLevel;
	
	
}
