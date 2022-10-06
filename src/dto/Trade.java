package dto;

import java.util.List;

public class Trade {

	private int tradeId;
	private String traderOne;
	private String traderTwo;
	private List<TradeItem> itemsOne; //Maybe it should be Object Item(ItemName/ItemId and quantity)
	private List<TradeItem> itemsTwo;
	
	private TradeResult tradeResult;

	public int getTradeId() {
		return tradeId;
	}

	public void setTradeId(int tradeId) {
		this.tradeId = tradeId;
	}

	public String getTraderOne() {
		return traderOne;
	}

	public void setTraderOne(String traderOne) {
		this.traderOne = traderOne;
	}

	public String getTraderTwo() {
		return traderTwo;
	}

	public void setTraderTwo(String traderTwo) {
		this.traderTwo = traderTwo;
	}

	public List<TradeItem> getItemsOne() {
		return itemsOne;
	}

	public void setItemsOne(List<TradeItem> itemsOne) {
		this.itemsOne = itemsOne;
	}

	public List<TradeItem> getItemsTwo() {
		return itemsTwo;
	}

	public void setItemsTwo(List<TradeItem> itemsTwo) {
		this.itemsTwo = itemsTwo;
	}

	public TradeResult getTradeResult() {
		return tradeResult;
	}

	public void setTradeResult(TradeResult tradeResult) {
		this.tradeResult = tradeResult;
	}
	
	
	
	
}
