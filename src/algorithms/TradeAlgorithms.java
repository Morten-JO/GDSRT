package algorithms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import controllers.ItemDataController;
import controllers.UserController;
import data.ItemData;
import data.PricePoint;
import dto.Percentage;
import dto.Trade;
import dto.TradeItem;
import dto.TradeResult;
import dto.TradeResult.TradeCalculated;
import util.DataTypeGenerator;
import util.ValueUtil;

public class TradeAlgorithms {

	public static Trade processTrade(Trade trade, ItemDataController itemDataController, UserController userController) {
		TradeResult result;
		PricePoint valueOne = new PricePoint(0,0,0);
		PricePoint valueTwo = new PricePoint(0,0,0);
		int itemsNotRegistered = 0;
		
		
		List<TradeItem> tradeOneNotRegistered = new ArrayList<>();
		float itemsOneAverageCertainty = 0f;
		int itemsOneCount = 0;
		HashMap<String, ItemData> valuesExtractedOne = new HashMap<>();
		for(TradeItem item : trade.getItemsOne()) {
			itemsOneCount += item.getQuantity();
			ItemData value = itemDataController.getItem(item.getItemId());
			if(value != null) {
				if(value.getEstimatedPrice().getMedianPrice() <= 0.001f) {
					value.setEstimatedPrice(new PricePoint(100f, 100f, 100f));
				}
				valuesExtractedOne.put(item.getItemId(), value);
				itemsOneAverageCertainty += value.getItemValueCertaintyPercentage().getPercentage();
				valueOne.incrementAll(item.getQuantity(), value.getEstimatedPrice());
			} else {
				itemsNotRegistered++;
				tradeOneNotRegistered.add(item);
				valueOne.incrementAll(item.getQuantity(), new PricePoint(100f, 100f, 100f));
			}
		}
		itemsOneAverageCertainty /= trade.getItemsOne().size();
		
		
		
		List<TradeItem> tradeTwoNotRegistered = new ArrayList<>();
		float itemsTwoAverageCertainty = 0f;
		int itemsTwoCount = 0;
		HashMap<String, ItemData> valuesExtractedTwo = new HashMap<>();
		for(TradeItem item : trade.getItemsTwo()) {
			itemsTwoCount += item.getQuantity();
			ItemData value = itemDataController.getItem(item.getItemId());
			if(value != null) {
				if(value.getEstimatedPrice().getMedianPrice() <= 0.001f) {
					value.setEstimatedPrice(new PricePoint(100f, 100f, 100f));
				}
				valuesExtractedTwo.put(item.getItemId(), value);
				itemsTwoAverageCertainty += value.getItemValueCertaintyPercentage().getPercentage();
				valueTwo.incrementAll(item.getQuantity(), value.getEstimatedPrice());
			} else {
				itemsNotRegistered++;
				tradeTwoNotRegistered.add(item);
				valueTwo.incrementAll(item.getQuantity(), new PricePoint(100f, 100f, 100f));
			}
		}
		itemsTwoAverageCertainty /= trade.getItemsTwo().size();
		
		boolean tradeNulled = false;
		int totalItems = trade.getItemsOne().size() + trade.getItemsTwo().size();
		if(totalItems / 3.0f < itemsNotRegistered) {
			tradeNulled = true;
		}
		int pricePerItemOne = 0;
		if(trade.getItemsOne().size() > 0) {
			pricePerItemOne = (int) (valueOne.getMedianPrice() / (itemsOneCount));
		}
		int pricePerItemTwo = 0;
		if(trade.getItemsTwo().size() > 0) {
			pricePerItemTwo = (int) (valueTwo.getMedianPrice() / (itemsTwoCount));
		}
		int averagePricePerItem;
		if(pricePerItemOne != 0 && pricePerItemTwo != 0) {
			averagePricePerItem = (pricePerItemOne + pricePerItemTwo) / 2;
		} else if(pricePerItemOne != 0) {
			averagePricePerItem = pricePerItemOne;
		} else if(pricePerItemTwo != 0) {
			averagePricePerItem = pricePerItemTwo;
		} else {
			result = new TradeResult(TradeCalculated.INCONCLUSIVE, 0, 0, 0, 0, DataTypeGenerator.generateCheckSum(trade));
			trade.setTradeResult(result);
			return trade;
		}
		for(TradeItem item : tradeOneNotRegistered) {
			int itemValue = averagePricePerItem / item.getQuantity();
			try {
				itemDataController.addItem(item.getItemId(), itemValue, new Percentage(0));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		for(TradeItem item : tradeTwoNotRegistered) {
			int itemValue = averagePricePerItem / item.getQuantity();
			try {
				itemDataController.addItem(item.getItemId(), itemValue, new Percentage(0));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(tradeNulled) {
			result = new TradeResult(TradeCalculated.COMPLETED, Math.abs(valueOne.getMedianPrice() - valueTwo.getMedianPrice()), Math.abs(valueOne.getMinimumPrice() - valueTwo.getMinimumPrice()), Math.abs(valueOne.getMaximumPrice() - valueTwo.getMaximumPrice()), 0, DataTypeGenerator.generateCheckSum(trade));
		} else {
			result = new TradeResult(TradeCalculated.FINALIZED, Math.abs(valueOne.getMedianPrice() - valueTwo.getMedianPrice()), Math.abs(valueOne.getMinimumPrice() - valueTwo.getMinimumPrice()), Math.abs(valueOne.getMaximumPrice() - valueTwo.getMaximumPrice()), 0, DataTypeGenerator.generateCheckSum(trade));
			if(valueTwo.isInBounds(valueOne)) {
				result.setTradeWarningLevel(0);
			} else {
				List<Integer> values = new ArrayList<>();
				for(TradeItem item : trade.getBothTradeItems()) {
					ItemData value = itemDataController.getItem(item.getItemId());
					if(value == null) {
						values.add(0);
					} else {
						values.add(value.getItemValueCertaintyPercentage().getPercentage());
					}
				}
				int averageConfidenceLevelPercentage = 0;
				if(values.size() > 0) {
					averageConfidenceLevelPercentage = values.stream().mapToInt(a -> a).sum() / values.size();
				}
				if(averageConfidenceLevelPercentage >= 50) {
					int valueDif = (int)Math.abs(valueOne.getMedianPrice() - valueTwo.getMedianPrice());
					float valueMultiplierDifference = 10;
					int minValueTrade = (int)Math.min(valueOne.getMedianPrice(), valueTwo.getMedianPrice());
					if(valueMultiplierDifference > 0) {
						valueMultiplierDifference = 10f * ((float)valueDif / (float)minValueTrade);
					}
					int warningLevel = (int) Math.min(10, Math.max(0, (averageConfidenceLevelPercentage / 100f) * valueMultiplierDifference));
					result.setTradeWarningLevel(warningLevel);
				}
			}
		}
		boolean itemsOneToCalcFor = false;
		if(ValueUtil.isAroundEqual(itemsTwoAverageCertainty, itemsOneAverageCertainty, 0.1f)) {
			if(itemsOneAverageCertainty <= 0.05f) {
				if(valueOne.getMedianPrice() > valueTwo.getMedianPrice()) {
					itemsOneToCalcFor = true;
				}
			} else {
				if(itemsOneCount > itemsTwoCount) {
					itemsOneToCalcFor = true;
				}
			}
		} else {
			if(itemsOneAverageCertainty > itemsTwoAverageCertainty) {
				itemsOneToCalcFor = true;
			}
		}
		//Update prices
		if(itemsOneToCalcFor) {
			for(TradeItem item : trade.getItemsTwo()) {
				if(tradeTwoNotRegistered.contains(item)) {
					continue;
				}
				float newValue;
				float value = valueOne.getMedianPrice();
				float valueWithoutThis;
				ItemData data = null;
				if(valuesExtractedTwo.containsKey(item.getItemId())) {
					valueWithoutThis = valueTwo.getMedianPrice() - valuesExtractedTwo.get(item.getItemId()).getEstimatedPrice().getMedianPrice() * item.getQuantity();
					newValue = valuesExtractedTwo.get(item.getItemId()).getEstimatedPrice().getMedianPrice();
					data = valuesExtractedTwo.get(item.getItemId());
				} else {
					valueWithoutThis = valueTwo.getMedianPrice() - 100f * item.getQuantity();
					newValue = 100f;
				}
				
				float valueDiff = value - valueTwo.getMedianPrice();
				if(!ValueUtil.isAroundEqual(valueDiff, 0f, 0.1f)) {
					if(valueDiff < 0f) {
						newValue = Math.max(newValue + valueDiff / item.getQuantity(), newValue/2.0f);
					} else {
						newValue = Math.min(newValue + valueDiff / item.getQuantity(), newValue + newValue/2.0f);
					}
				}
				try {
					Percentage percentage;
					if(data != null) {
						percentage = data.getItemValueCertaintyPercentage();
						if((data.getTotalTrades() + 1) % 5 == 0 && percentage.getPercentage() < 100) {
							percentage.setPercentage(percentage.getPercentage() + 1);
						}
					} else {
						percentage = new Percentage(0);
					}
					itemDataController.addItem(item.getItemId(), newValue, percentage);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			for(TradeItem item : trade.getItemsOne()) {
				if(tradeOneNotRegistered.contains(item)) {
					continue;
				}
				try {
					itemDataController.increaseTotalTrades(item.getItemId());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else {
			for(TradeItem item : trade.getItemsOne()) {
				if(tradeOneNotRegistered.contains(item)) {
					continue;
				}
				float newValue;
				float value = valueTwo.getMedianPrice();
				float valueWithoutThis;
				ItemData data = null;
				if(valuesExtractedOne.containsKey(item.getItemId())) {
					valueWithoutThis = valueOne.getMedianPrice() - valuesExtractedOne.get(item.getItemId()).getEstimatedPrice().getMedianPrice() * item.getQuantity();
					newValue = valuesExtractedOne.get(item.getItemId()).getEstimatedPrice().getMedianPrice();
					data = valuesExtractedOne.get(item.getItemId());
				} else {
					valueWithoutThis = valueOne.getMedianPrice() - 100f * item.getQuantity();
					newValue = 100f;
				}
				float valueDiff = value - valueOne.getMedianPrice();
				if(!ValueUtil.isAroundEqual(valueDiff, 0f, 0.1f)) {
					if(valueDiff < 0f) {
						newValue = Math.max(newValue + valueDiff / item.getQuantity(), newValue/2.0f);
					} else {
						newValue = Math.min(newValue + valueDiff / item.getQuantity(), newValue + newValue/2.0f) / item.getQuantity();
					}
				}
				
				try {
					Percentage percentage;
					if(data != null) {
						percentage = data.getItemValueCertaintyPercentage();
						if((data.getTotalTrades() + 1) % 5 == 0 && percentage.getPercentage() < 100) {
							percentage.setPercentage(percentage.getPercentage() + 1);
						}
					} else {
						percentage = new Percentage(0);
					}
					itemDataController.addItem(item.getItemId(), newValue, percentage);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			for(TradeItem item : trade.getItemsTwo()) {
				if(tradeTwoNotRegistered.contains(item)) {
					continue;
				}
				try {
					itemDataController.increaseTotalTrades(item.getItemId());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		if(result.getTradeWarningLevel() > 5) {
			userController.requestUserCheck(trade.getTraderOne());
			userController.requestUserCheck(trade.getTraderTwo());
			
		}
		
		trade.setTradeResult(result);
		
		return trade;
	}
	
}
