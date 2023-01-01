package extensions;

import data.ItemData;
import data.PricePoint;
import data_retrievers.IItemDataRetriever;

public class TestItemDataRetriever implements IItemDataRetriever{

	@Override
	public ItemData getItem(String itemId) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean updatePrice(String itemId, PricePoint pricePoint) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateRecentTrades(String itemId, Integer[] recentTrades) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateTotalTrades(String itemId, int totalTrades) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateCertaintyPercentage(String itemId, int certaintyPercentage) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addItem(String itemId, PricePoint pricePoint, Integer[] recentTrades, int totalTrades,
			int certaintyPercentage) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean itemExists(String itemId) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean incrementTotalTrades(String itemId) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

}
