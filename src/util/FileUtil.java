package util;

import java.io.FileInputStream;
import java.util.Properties;

import config.LoadedConfigs;

public class FileUtil {

	private static final String CONFIG_LOCATION = "res/config.properties";
	
	public static boolean loadConfigs() {
		try {
            FileInputStream propsInput = new FileInputStream(CONFIG_LOCATION);
            Properties prop = new Properties();
            prop.load(propsInput);
            LoadedConfigs.DB_HOST = prop.getProperty("DB_HOST");
            LoadedConfigs.DB_USER = prop.getProperty("DB_USERNAME");
            LoadedConfigs.DB_PASS = prop.getProperty("DB_PASSWORD");
            LoadedConfigs.DB_TYPE = LoadedConfigs.DatabaseType.valueOf(prop.getProperty("DB_TYPE"));
            LoadedConfigs.STRICTNESS_LEVEL = Integer.valueOf(prop.getProperty("STRICTNESS_LEVEL"));
            propsInput.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
		return false;
	}
	
}
