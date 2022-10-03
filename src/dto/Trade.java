package dto;

import java.util.List;

public class Trade {

	private String traderOne;
	private String traderTwo;
	private List<TradeItem> itemsOne; //Maybe it should be Object Item(ItemName/ItemId and quantity)
	private List<TradeItem> itemsTwo;
	
	private TradeResult tradeResult;
	
	
}
