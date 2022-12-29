package util;

import java.util.HashMap;
import java.util.Map;

public class AdditionalDataConverter {

	public static Map<String, String> stringToMap(String str){
		Map<String, String> map = new HashMap<>();
		String[] sets = str.split(",");
		for(String set : sets) {
			String[] values = set.split(":");
			if(values.length >= 2) {
				map.put(values[0], values[1]);
			}
		}
		return map;
	}
	
	public static String mapToString(Map<String, String> map) {
		String str = "";
		boolean first = true;
		for(String key : map.keySet()) {
			if(first) {
				first = false;
			} else {
				str += ",";
			}
			str += key + ":" + map.get(key);
		}
		return str;
	}
	
}
