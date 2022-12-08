package config;

public class LoadedConfigs {

	public enum DatabaseType {
		POSTGRESQL
	}
	
	public enum ConnectionType {
		SOCKET
	}
	
	public static DatabaseType DB_TYPE = DatabaseType.POSTGRESQL; 
	public static String DB_USER="";
	public static String DB_HOST="";
	public static String DB_PASS="";
	public static String DB_NAME="";
	public static int DB_PORT=5432;
	public static Integer STRICTNESS_LEVEL = 1;
	public static ConnectionType CONNECTION_TYPE  = ConnectionType.SOCKET;
	public static int INCOMING_SERVER_PORT = 1234;
	
}
