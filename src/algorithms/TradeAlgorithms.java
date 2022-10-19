package algorithms;

import java.util.ArrayList;
import java.util.List;

import controllers.ItemDataController;
import data.ItemData;
import dto.Percentage;
import dto.Trade;
import dto.TradeItem;
import dto.TradeResult;
import dto.TradeResult.TradeCalculated;
import util.CheckSumGenerator;
import util.DateStamper;

public class TradeAlgorithms {

	public Trade processTrade(Trade trade, ItemDataController itemDataController) {
		TradeResult result;
		int valueOne = 0;
		int valueTwo = 0;
		int itemsNotRegistered = 0;
		List<TradeItem> tradeOneNotRegistered = new ArrayList<>();
		for(TradeItem item : trade.getItemsOne()) {
			ItemData value = itemDataController.retrieveItemData(item.getItemId());
			if(value != null) {
				valueOne += item.getQuantity() * value.getEstimatedMedianPrice();
			} else {
				itemsNotRegistered++;
				tradeOneNotRegistered.add(item);
			}
		}
		List<TradeItem> tradeTwoNotRegistered = new ArrayList<>();
		for(TradeItem item : trade.getItemsTwo()) {
			ItemData value = itemDataController.retrieveItemData(item.getItemId());
			if(value != null) {
				valueTwo += item.getQuantity() * value.getEstimatedMedianPrice();
			} else {
				itemsNotRegistered++;
				tradeTwoNotRegistered.add(item);
			}
		}
		boolean tradeNulled = false;
		int totalItems = trade.getItemsOne().size() + trade.getItemsTwo().size();
		if(totalItems / 3.0f < itemsNotRegistered) {
			tradeNulled = true;
		}
		if(tradeNulled) {
			//TODO estimate prices and add
			int pricePerItemOne = 0;
			if(trade.getItemsOne().size() > 0) {
				pricePerItemOne = valueOne / (trade.getItemsOne().size() - tradeOneNotRegistered.size());
			}
			int pricePerItemTwo = 0;
			if(trade.getItemsTwo().size() > 0) {
				pricePerItemTwo = valueTwo / (trade.getItemsTwo().size() - tradeTwoNotRegistered.size());
			}
			int averagePricePerItem;
			if(pricePerItemOne != 0 && pricePerItemTwo != 0) {
				averagePricePerItem = (pricePerItemOne + pricePerItemTwo) / 2;
			} else if(pricePerItemOne != 0) {
				averagePricePerItem = pricePerItemOne;
			} else if(pricePerItemTwo != 0) {
				averagePricePerItem = pricePerItemTwo;
			} else {
				result = new TradeResult(TradeCalculated.INCONCLUSIVE, 0, 0, 0, 0, CheckSumGenerator.generateCheckSum(trade));
				trade.setTradeResult(result);
				return trade;
			}
			for(TradeItem item : tradeOneNotRegistered) {
				int itemValue = averagePricePerItem / item.getQuantity();
				itemDataController.addItem(item.getItemId(), itemValue, new Percentage(0));
			}
			for(TradeItem item : tradeTwoNotRegistered) {
				int itemValue = averagePricePerItem / item.getQuantity();
				itemDataController.addItem(item.getItemId(), itemValue, new Percentage(0));
			}
			result = new TradeResult(TradeCalculated.COMPLETED, Math.abs(valueOne - valueTwo), Math.abs(valueOne - valueTwo), Math.abs(valueOne - valueTwo), 0, CheckSumGenerator.generateCheckSum(trade));
		} else {
			
		}
		//if(check itemsNotRegistered and make a choice if the trade should be "nulled" or should count)
		//First, assign values to each item in trade, if available, then compare/mark possibly inconclusive
		//Second, add/updates values to the itemdata storage
		//Third(IF), if first is found to be "sketch", then look into trade history of individuals, if none or little wrong, dont do anything
		//else increase eyeLevel
		//Should eyeLevel be above certain threshold, send warning maybe? Maybe it shouldn't be here
		//Set result, then return that shit
		//Problem: What if all items are not registered?
		
		return trade;
	}
	
}
