package controllers;

import data.ItemData;
import data_retrievers.IItemDataRetriever;
import dto.Percentage;

public class ItemDataController {

	//TODO Maybe make it so an transistent version of the items are present.
	private IItemDataRetriever idr;
	
	public ItemDataController(IItemDataRetriever idr) {
		this.idr = idr;
	}
	
	/**
	 * Can only be called when loadedItemDataMap is here
	 * @param itemId
	 * @param value
	 * @return
	 * @throws Exception 
	 */
	public ItemData addItem(String itemId, float value, Percentage itemCertaintyPercentage) throws Exception {
		if(idr.itemExists(itemId)) {
			ItemData data = idr.getItem(itemId);
			idr.updateTotalTrades(itemId, data.getTotalTrades()+1);
			if(data.getItemValueCertaintyPercentage().getPercentage() < itemCertaintyPercentage.getPercentage()) {
				idr.updateCertaintyPercentage(itemId, itemCertaintyPercentage.getPercentage());
			}
			data.getEstimatedPrice().calculatePricesBasedOnValue(value);
			idr.updatePrice(itemId, data.getEstimatedPrice());
			return data;
		} else {
			ItemData data = new ItemData();
			data.getEstimatedPrice().setMaximumPrice(value);
			data.getEstimatedPrice().setMinimumPrice(value);
			data.getEstimatedPrice().setMedianPrice(value);
			data.setItemId(itemId);
			data.setItemValueCertaintyPercentage(itemCertaintyPercentage);
			data.setRecentTradeValues(new Integer[] {(int)value});
			data.setTotalTrades(1);
			if(idr.addItem(itemId, data.getEstimatedPrice(), data.getRecentTradeValues(), data.getTotalTrades(), data.getItemValueCertaintyPercentage().getPercentage())) {
				return data;
			} else {
				return null;
			}
		}
	}
	
	public ItemData getItem(String itemId) {
		try {
			return idr.getItem(itemId);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
