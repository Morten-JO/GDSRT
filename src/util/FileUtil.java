package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
            LoadedConfigs.DB_NAME = prop.getProperty("DB_NAME");
            LoadedConfigs.DB_TYPE = LoadedConfigs.DatabaseType.valueOf(prop.getProperty("DB_TYPE").toUpperCase());
            LoadedConfigs.STRICTNESS_LEVEL = Integer.valueOf(prop.getProperty("STRICTNESS_LEVEL"));
            LoadedConfigs.CONNECTION_TYPE = LoadedConfigs.ConnectionType.valueOf(prop.getProperty("CONNECTION_TYPE").toUpperCase());
            LoadedConfigs.ENCRYPTION = Boolean.valueOf(prop.getProperty("ENCRYPTION"));
            LoadedConfigs.FLOOD_PRICES = Boolean.valueOf(prop.getProperty("FLOOD_ITEMS"));
            LoadedConfigs.FLOOD_PRICES_PATH = prop.getProperty("FLOOD_ITEMS_PATH");
            LoadedConfigs.INCOMING_SERVER_PORT = Integer.valueOf(prop.getProperty("SERVER_PORT"));
            propsInput.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
		return false;
	}
	
	public static List<List<String>> readCSVFile(String path) throws IOException {
		File file = new File(path);
		if(!file.exists()) {
			throw new FileNotFoundException(path);
		}
		FileReader reader = new FileReader(file);
		List<List<String>> records = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(reader)) {
		    String line;
		    while ((line = br.readLine()) != null) {
		        String[] values = line.split(",");
		        records.add(Arrays.asList(values));
		    }
		}
		return records;
	}
	
	public static Map<String, Float> readItemFloodCSVFile(String path){
		Map<String, Float> map = new HashMap<>();
		try {
			List<List<String>> csvFile = readCSVFile(path);
			for(List<String> entry : csvFile) {
				if(entry.size() >= 2) {
					try {
						float val = Float.parseFloat(entry.get(1));
						map.put(entry.get(0), val);
					} catch(NumberFormatException e) {
						e.printStackTrace();
						continue;
					}
				}
			}
			return map;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
}
