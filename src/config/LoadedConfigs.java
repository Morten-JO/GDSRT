package config;

public class LoadedConfigs {

	public enum DatabaseType {
		POSTGRESQL, TEMP
	}

	public enum ConnectionType {
		SOCKET
	}

	public static DatabaseType DB_TYPE = DatabaseType.TEMP;
	public static String DB_USER="";
	public static String DB_HOST="";
	public static String DB_PASS="";
	public static String DB_NAME="";
	public static int DB_PORT=5432;
	public static Integer STRICTNESS_LEVEL = 1;
	public static ConnectionType CONNECTION_TYPE  = ConnectionType.SOCKET;
	public static int INCOMING_SERVER_PORT = 1234;
	public static boolean ENCRYPTION = false;
	public static String PRIVATE_KEY_PATH = "";
	public static boolean FLOOD_PRICES = false;
	public static String FLOOD_PRICES_PATH = "items.csv";

}
