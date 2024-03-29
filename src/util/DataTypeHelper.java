package util;

import java.sql.Array;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import dto.Trade;
import dto.TradeItem;

public class DataTypeHelper {

	public static Integer[] sqlArrayToIntArray(Array array) {
		Integer[] vals;
		try {
			vals = (Integer[])array.getArray();
			return vals;
		} catch (SQLException e) {
			return null;
		}

	}

	public static Array intArrayToSqlArray(Integer[] array, Connection connection) {
		Array conv;
		try {
			conv = connection.createArrayOf("INT", array);
		} catch (SQLException e) {
			return null;
		}
		return conv;
	}

	public static List<TradeItem> stringToTradeItemList(String string){
		String[] vals;
		try {
			vals = string.split(",");
			List<TradeItem> items = new ArrayList<>();
			for(String elem : vals) {
				String[] dataSplit = elem.split(";");
				TradeItem item = new TradeItem(dataSplit[0], Integer.parseInt(dataSplit[1]));
				items.add(item);
			}
			return items;
		} catch (Exception e) {
			return null;
		}
	}

	public static String tradeItemListToString(List<TradeItem> list, Connection connection) {
		String vals = "";
		for(int i = 0; i < list.size(); i++) {
			String value = list.get(i).getItemId()+";"+list.get(i).getQuantity();
			vals += value;
			if(i != list.size() - 1) {
				vals += ",";
			}
		}
		return vals;
	}

	public static Trade tradeStringToTrade(String trade) {
		Map<String, String> map = Arrays.stream(trade.replace("{", "").replace("}", "").split(","))
				.map(entry -> entry.split("="))
			    .collect(Collectors.toMap(entry -> entry[0], entry -> entry[1]));
		Trade convertedTrade = new Trade();
		convertedTrade.setTraderOne(map.get("traderOne"));
		convertedTrade.setTraderTwo(map.get("traderTwo"));
		convertedTrade.setItemsOne(mapToTradeItems(map, "traderOne"));
		convertedTrade.setItemsTwo(mapToTradeItems(map, "traderTwo"));
		return convertedTrade;
	}

	private static List<TradeItem> mapToTradeItems(Map<String, String> map, String prefix){
		List<TradeItem> items = new ArrayList<>();
		int iterator = 1;
		while(true) {
			if(map.containsKey(prefix+iterator)) {
				String[] tradeItemData = map.get(prefix+iterator).split(":");
				TradeItem item = new TradeItem(tradeItemData[0], Integer.valueOf(tradeItemData[1]));
				items.add(item);
				iterator++;
			} else {
				break;
			}
		}
		return items;
	}


}
