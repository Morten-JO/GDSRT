package util;

import java.sql.Array;
import java.sql.Connection;
import java.sql.SQLException;

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
	
	
}
