package main;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import java.util.Map;

import config.LoadedConfigs;
import config.LoadedConfigs.ConnectionType;
import connection.Server;
import controllers.ItemDataController;
import controllers.TradeController;
import controllers.UserController;
import data_retrievers.IItemDataRetriever;
import data_retrievers.ITradeDataRetriever;
import data_retrievers.IUserDataRetriever;
import db.DatabaseController;
import db.DatabaseItemDataRetriever;
import db.DatabaseTradeDataRetriever;
import db.DatabaseUserDataRetriever;
import dto.Percentage;
import temp_db.TempDatabaseItemDataRetriever;
import temp_db.TempDatabaseTradeDataRetriever;
import temp_db.TempDatabaseUserDataRetriever;
import util.DebugDocumentLogger;
import util.EncryptionHelper;
import util.FileUtil;
import util.PrintDebugDocumentLogger;

public class Main {

	public static void main(String[] args) {
		if(FileUtil.loadConfigs()) {
			System.out.println("Configs were loaded....");
		}
		DebugDocumentLogger logger = null;
		try {
			logger = new DebugDocumentLogger(new File("gdsrt_debug.txt"));
		} catch (IOException e) {
			System.err.println("Failed to load logger.. Disabling documentlogger.");
			logger = new PrintDebugDocumentLogger();
		}

		DatabaseController dbc = null;
		ITradeDataRetriever tdr = null;
		IUserDataRetriever udr = null;
		IItemDataRetriever idr = null;
		if(LoadedConfigs.DB_TYPE == LoadedConfigs.DatabaseType.POSTGRESQL) {
			try {
				dbc = new DatabaseController(LoadedConfigs.DB_HOST, LoadedConfigs.DB_TYPE.toString(), LoadedConfigs.DB_NAME, LoadedConfigs.DB_USER, LoadedConfigs.DB_PASS, LoadedConfigs.DB_PORT, logger);
				udr = new DatabaseUserDataRetriever(dbc);
				tdr = new DatabaseTradeDataRetriever(dbc);
				idr = new DatabaseItemDataRetriever(dbc);
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}

		} else {
			if(!(LoadedConfigs.DB_TYPE == LoadedConfigs.DatabaseType.TEMP)) {
				System.err.println("Known DB type not specified, using transient database(DATA WILL BE DELETED ON SHUTDOWN).");
			} else {
				System.out.println("Loading transient database, please be aware that data is deleted on shutdown.");
			}
			tdr = new TempDatabaseTradeDataRetriever();
			udr = new TempDatabaseUserDataRetriever();
			idr = new TempDatabaseItemDataRetriever();
		}
		if(dbc == null && tdr == null && udr == null && idr == null) {
			System.err.println("Failed to etablish database connection, shutting off.");
			System.exit(0);
			return;
		}
		TradeController tc = new TradeController(tdr);
		ItemDataController ic = new ItemDataController(idr);
		UserController uc = new UserController(tc, udr, tdr, ic, logger);

		//Floods prices into db
		if(LoadedConfigs.FLOOD_PRICES) {
			Map<String, Float> data = FileUtil.readItemFloodCSVFile("res/"+LoadedConfigs.FLOOD_PRICES_PATH);
			if(data != null) {
				for(Map.Entry<String, Float> entry : data.entrySet()) {
					try {
						ic.addItem(entry.getKey(), entry.getValue(), new Percentage(100));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				logger.writeLineToFile("Finishing flooding item prices.");
			} else {
				logger.writeLineToFile("Failed to flood item prices.");
			}
		}

		if(LoadedConfigs.CONNECTION_TYPE == ConnectionType.SOCKET) {
			Server server;
			try {
				if(logger instanceof DebugDocumentLogger) {
					server = new Server(tc, uc, ic, logger);
				} else {
					server = new Server(tc, uc, ic);
				}
			} catch (IOException e) {
				e.printStackTrace();
				System.err.println("Failed to start server...");
				System.exit(0);
				return;
			}
			if(LoadedConfigs.ENCRYPTION) {
				File file = new File(LoadedConfigs.PRIVATE_KEY_PATH);
				try {
					PrivateKey key = EncryptionHelper.getKeyFileToPrivateKey(file);
					server.setDecryptionKey(key);
					System.out.println("Succesfully loaded key.");
				} catch (NoSuchAlgorithmException | InvalidKeySpecException | IOException e) {
					e.printStackTrace();
					System.err.println("Failed to load private key... Ensure path is correct");
					System.exit(0);
				}
			}
			
			System.out.println("Server succesfully started..");
			System.out.println("Server is up and running on port: "+LoadedConfigs.INCOMING_SERVER_PORT);
			server.run();

		} else {
			System.err.println("CONNECTION_TYPE is not set in configs, only available option as it stands now is: SOCKET");
		}


	}

}
