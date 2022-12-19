package algorithms;

import java.util.Map;

import data.ItemData;
import data.PricePoint;
import data_retrievers.IItemDataRetriever;

public class PriceFlooder {

	//Here you can import prices for itemids to better "train" the algorithm to spot "uneven" trades
	public static boolean importPrices(Map<String, Float> prices, IItemDataRetriever idr) {
		for(String key : prices.keySet()) {
			Float value = prices.get(key);
			ItemData itemData = null;
			try {
				itemData = idr.getItem(key);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(itemData != null) {
				try {
					idr.addItem(key, new PricePoint(value, value, value), new Integer[] {}, 0, 50);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return true;
	}
	
	
}
