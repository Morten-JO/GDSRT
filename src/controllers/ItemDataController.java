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
	public void addItem(String itemId, int value) {
		if(itemDataMap.containsKey(itemId)) {
			System.out.println("Already here");
			ItemData data = itemDataMap.get(itemId);
			data.incrementTotalTrades();
			//TODO
		} else {
			System.out.println("Not here");
			ItemData data = new ItemData();
			data.setEstimatedMaximumPrice(value);
			data.setEstimatedMinimumPrice(value);
			data.setEstimatedMedianPrice(value);
			data.setItemId(itemId);
			data.setItemValueCertaintyPercentage(new Percentage(0));
			data.setRecentTradeValues(new int[] {value});
			data.setTotalTrades(1);
			itemDataMap.put(itemId, data);
		}
	}
	
	public ItemData retrieveItemData(String itemId) {
		return itemDataMap.get(itemId);
	}
	
}
