package util;

import java.sql.Array;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
				TradeItem item = new TradeItem();
				String[] dataSplit = elem.split(";");
				item.setItemId(dataSplit[0]);
				item.setQuantity(Integer.parseInt(dataSplit[1]));
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
	
	
}
