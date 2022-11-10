package controllers;

import java.util.HashMap;
import java.util.Map;

import data.ItemData;
import dto.Percentage;

public class ItemDataController {

	private Map<String, ItemData> itemDataMap;
	private boolean loadedItemDataMap = false;
	
	public ItemDataController() {
		itemDataMap = new HashMap<>();
	}
	
	/**
	 * Can only be called when loadedItemDataMap is here ig?
	 * @param itemId
	 * @param value
	 * @return
	 */
	public ItemData addItem(String itemId, int value, Percentage itemCertaintyPercentage) {
		if(itemDataMap.containsKey(itemId)) {
			ItemData data = itemDataMap.get(itemId);
			data.incrementTotalTrades();
			if(data.getItemValueCertaintyPercentage().getPercentage() < itemCertaintyPercentage.getPercentage()) {
				data.getItemValueCertaintyPercentage().setPercentage(itemCertaintyPercentage.getPercentage());
			}
			return data;
		} else {
			ItemData data = new ItemData();
			data.getEstimatedPrice().setMaximumPrice(value);
			data.getEstimatedPrice().setMinimumPrice(value);
			data.getEstimatedPrice().setMedianPrice(value);
			data.setItemId(itemId);
			data.setItemValueCertaintyPercentage(itemCertaintyPercentage);
			data.setRecentTradeValues(new int[] {value});
			data.setTotalTrades(1);
			itemDataMap.put(itemId, data);
			return data;
		}
	}
	
	public ItemData retrieveItemData(String itemId) {
		return itemDataMap.get(itemId);
	}
	
}
