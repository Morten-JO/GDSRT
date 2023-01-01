package data_retrievers;

import data.ItemData;
import data.PricePoint;

public interface IItemDataRetriever {

	public ItemData getItem(String itemId) throws Exception;
	
	public boolean updatePrice(String itemId, PricePoint pricePoint) throws Exception;
	
	public boolean updateRecentTrades(String itemId, Integer[] recentTrades) throws Exception;
	
	public boolean updateTotalTrades(String itemId, int totalTrades) throws Exception;
	
	public boolean updateCertaintyPercentage(String itemId, int certaintyPercentage) throws Exception;
	
	public boolean addItem(String itemId, PricePoint pricePoint, Integer[] recentTrades, int totalTrades, int certaintyPercentage) throws Exception;
	
	public boolean itemExists(String itemId) throws Exception;
	
	public boolean incrementTotalTrades(String itemId) throws Exception;
	
}
