package main;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

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
import temp_db.TempDatabaseItemDataRetriever;
import temp_db.TempDatabaseTradeDataRetriever;
import temp_db.TempDatabaseUserDataRetriever;
import util.DebugDocumentLogger;
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
				tdr = new DatabaseTradeDataRetriever(dbc);
				udr = new DatabaseUserDataRetriever(dbc);
				idr = new DatabaseItemDataRetriever(dbc);
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
			
		} else {
			System.err.println("Known DB type not specified, using transient database(DATA WILL BE DELETED ON SHUTDOWN).");
			tdr = new TempDatabaseTradeDataRetriever();
			udr = new TempDatabaseUserDataRetriever();
			idr = new TempDatabaseItemDataRetriever();
		}
		if(dbc == null) {
			System.err.println("Failed to etablish database connection, shutting off.");
			System.exit(0);
			return;
		}
		TradeController tc = new TradeController(tdr);
		UserController uc = new UserController(tc, udr, tdr, idr);
		ItemDataController ic = new ItemDataController(idr);
		
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
				System.out.println("Failed to start server... Check error.");
				System.exit(0);
				return;
			}
			System.out.println("Server succesfully started..");
			server.run();
		} else {
			System.err.println("CONNECTION_TYPE is not set in configs, only available option as it stands now is: SOCKET");
		}
		
		
	}

}
