package extensions;

import java.util.ArrayList;
import java.util.List;

import data.ItemData;
import data.PricePoint;
import data_retrievers.IItemDataRetriever;
import dto.Percentage;

public class TestItemDataRetriever implements IItemDataRetriever{

	private List<ItemData> itemDatas = new ArrayList<>();
	
	@Override
	public ItemData getItem(String itemId) throws Exception {
		for(ItemData data : itemDatas) {
			if(data.getItemId().equals(itemId)) {
				return data;
			}
		}
		return null;
	}

	@Override
	public boolean updatePrice(String itemId, PricePoint pricePoint) throws Exception {
		for(ItemData data : itemDatas) {
			if(data.getItemId().equals(itemId)) {
				data.setEstimatedPrice(pricePoint);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean updateRecentTrades(String itemId, Integer[] recentTrades) throws Exception {
		for(ItemData data : itemDatas) {
			if(data.getItemId().equals(itemId)) {
				data.setRecentTradeValues(recentTrades);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean updateTotalTrades(String itemId, int totalTrades) throws Exception {
		for(ItemData data : itemDatas) {
			if(data.getItemId().equals(itemId)) {
				data.setTotalTrades(totalTrades);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean updateCertaintyPercentage(String itemId, int certaintyPercentage) throws Exception {
		for(ItemData data : itemDatas) {
			if(data.getItemId().equals(itemId)) {
				data.setItemValueCertaintyPercentage(new Percentage(certaintyPercentage));
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean addItem(String itemId, PricePoint pricePoint, Integer[] recentTrades, int totalTrades,
			int certaintyPercentage) throws Exception {
		ItemData data = new ItemData();
		data.setItemId(itemId);;
		data.setItemValueCertaintyPercentage(new Percentage(certaintyPercentage));
		data.setTotalTrades(totalTrades);
		data.setRecentTradeValues(recentTrades);
		data.setEstimatedPrice(pricePoint);
		return itemDatas.add(data);
	}

	@Override
	public boolean itemExists(String itemId) throws Exception {
		for(ItemData data : itemDatas) {
			if(data.getItemId().equals(itemId)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean incrementTotalTrades(String itemId) throws Exception {
		for(ItemData data : itemDatas) {
			if(data.getItemId().equals(itemId)) {
				data.setTotalTrades(data.getTotalTrades()+1);
				return true;
			}
		}
		return false;
	}

}
