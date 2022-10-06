package algorithms;

import controllers.ItemDataController;
import data.ItemData;
import dto.Trade;
import dto.TradeItem;
import dto.TradeResult;

public class TradeAlgorithms {

	public Trade processTrade(Trade trade, ItemDataController itemDataController) {
		TradeResult result = new TradeResult();
		int valueOne = 0;
		int valueTwo = 0;
		int itemsNotRegistered = 0;
		for(TradeItem item : trade.getItemsOne()) {
			ItemData value = itemDataController.retrieveItemData(item.getItemId());
			if(value != null) {
				valueOne += item.getQuantity() * value.getEstimatedMedianPrice();
			} else {
				itemsNotRegistered++;
			}
		}
		for(TradeItem item : trade.getItemsTwo()) {
			ItemData value = itemDataController.retrieveItemData(item.getItemId());
			if(value != null) {
				valueTwo += item.getQuantity() * value.getEstimatedMedianPrice();
			} else {
				itemsNotRegistered++;
			}
		}
		if(check itemsNotRegistered and make a choice if the trade should be "nulled" or should count)
		//First, assign values to each item in trade, if available, then compare/mark possibly inconclusive
		//Second, add/updates values to the itemdata storage
		//Third(IF), if first is found to be "sketch", then look into trade history of individuals, if none or little wrong, dont do anything
		//else increase eyeLevel
		//Should eyeLevel be above certain threshold, send warning maybe? Maybe it shouldn't be here
		//Set result, then return that shit
		
		
		return trade;
	}
	
}
