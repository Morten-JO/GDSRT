package config;

public class LoadedConfigs {

	public enum DatabaseType {
		POSTGRES
	}
	
	public static DatabaseType DB_TYPE = DatabaseType.POSTGRES; 
	public static String DB_USER="";
	public static String DB_HOST="";
	public static String DB_PASS="";
	public static Integer STRICTNESS_LEVEL = 1;
	
}
