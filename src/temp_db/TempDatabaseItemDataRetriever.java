package temp_db;

import java.util.HashMap;
import java.util.Map;

import data.ItemData;
import data.PricePoint;
import data_retrievers.IItemDataRetriever;
import dto.Percentage;

public class TempDatabaseItemDataRetriever implements IItemDataRetriever{

	private Map<String, ItemData> mapOfData;
	
	public TempDatabaseItemDataRetriever() {
		mapOfData = new HashMap<>();
	}
	
	@Override
	public ItemData getItem(String itemId) throws Exception {
		if(mapOfData.containsKey(itemId)) {
			return mapOfData.get(itemId);
		}
		return null;
	}

	@Override
	public boolean updatePrice(String itemId, PricePoint pricePoint) throws Exception {
		if(itemExists(itemId)) {
			ItemData data = mapOfData.get(itemId);
			data.setEstimatedPrice(pricePoint);
			mapOfData.put(itemId, data);
			return true;
		}
		return false;
	}

	@Override
	public boolean updateRecentTrades(String itemId, Integer[] recentTrades) throws Exception {
		if(itemExists(itemId)) {
			ItemData data = mapOfData.get(itemId);
			data.setRecentTradeValues(recentTrades);
			mapOfData.put(itemId, data);
			return true;
		}
		return false;
	}

	@Override
	public boolean updateTotalTrades(String itemId, int totalTrades) throws Exception {
		if(itemExists(itemId)) {
			ItemData data = mapOfData.get(itemId);
			data.setTotalTrades(totalTrades);
			mapOfData.put(itemId, data);
			return true;
		}
		return false;
	}

	@Override
	public boolean updateCertaintyPercentage(String itemId, int certaintyPercentage) throws Exception {
		if(itemExists(itemId)) {
			ItemData data = mapOfData.get(itemId);
			data.setItemValueCertaintyPercentage(new Percentage(certaintyPercentage));
			mapOfData.put(itemId, data);
			return true;
		}
		return false;
	}

	@Override
	public boolean addItem(String itemId, PricePoint pricePoint, Integer[] recentTrades, int totalTrades,
			int certaintyPercentage) throws Exception {
		if(itemExists(itemId)) {
			return false;
		}
		ItemData data = new ItemData();
		data.setItemId(itemId);
		data.setEstimatedPrice(pricePoint);
		data.setRecentTradeValues(recentTrades);
		data.setTotalTrades(totalTrades);
		data.setItemValueCertaintyPercentage(new Percentage(certaintyPercentage));
		return true;
	}

	@Override
	public boolean itemExists(String itemId) throws Exception {
		return mapOfData.containsKey(itemId);
	}

	@Override
	public boolean incrementTotalTrades(String itemId) throws Exception {
		if(itemExists(itemId)) {
			return false;
		}
		ItemData data = new ItemData();
		data.setItemId(itemId);
		data.setTotalTrades(data.getTotalTrades() + 1);
		return true;
	}

}
