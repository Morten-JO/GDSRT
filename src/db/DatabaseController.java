package db;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLNonTransientConnectionException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import util.DebugDocumentLogger;

public class DatabaseController {

	Connection connect = null;
	String databaseName;
	DebugDocumentLogger documentLogger;

	private String databaseUrl;
	private String databaseUsername;
	private String databasePassword;

	private long timeOnLastQuery;
	private final long TIME_PER_KEEP_ALIVE_QUERY = 900000L;
	private Thread keepAliveThread;
	private boolean databaseConnectionRunning = false;

	public DatabaseController(String databaseHost, String type, String databaseName, String username, String password, int port, DebugDocumentLogger documentLogger) throws ClassNotFoundException, SQLException {
		this.databaseName = databaseName;
		this.documentLogger = documentLogger;
		String url = "jdbc:"+type.toLowerCase()+"://"+databaseHost+":"+port+"/"+databaseName;
		Connection conn = DriverManager.getConnection(url, username, password);
		this.databaseUrl = url;
		this.databaseUsername = username;
		this.databasePassword = password;
		connect = conn;
		timeOnLastQuery = System.currentTimeMillis();
		databaseConnectionRunning = true;
		keepAliveThread = new Thread(new Runnable() {

			@Override
			public void run() {
				while (databaseConnectionRunning) {
					if (System.currentTimeMillis() - timeOnLastQuery > TIME_PER_KEEP_ALIVE_QUERY) {
						try {
							PreparedStatement statement = prepareSafeStatement("select * from " + databaseName + ".users where USER_ID = ?");
							statement.setString(1, "");
							statement.executeQuery();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					} else {
						try {
							Thread.sleep(30000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}
		});
		keepAliveThread.start();

		checkDatabaseDetails();
	}

	private boolean checkDatabaseDetails() {
		ResultSet resultSet;
		List<String> tableNames = new ArrayList<>();
		try {
			resultSet = connect.getMetaData().getTables(null, null, null, new String[] {"TABLE"});
			while (resultSet.next()) {
			    String name = resultSet.getString("TABLE_NAME");
			    String schema = resultSet.getString("TABLE_SCHEM");
			    tableNames.add(name);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// check player
		try {
			if(!tableNames.contains("users")) {
				Statement statement = connect.createStatement();
				statement.executeQuery("select 1 from " + databaseName + ".users limit 1;");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				createUsersTable();
				documentLogger.writeLineToFile("Database - Created Users table");
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		try {
			if(!tableNames.contains("trades")) {
				Statement statement = connect.createStatement();
				statement.executeQuery("select 1 from " + databaseName + ".trades limit 1;");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				createTradesTable();
				documentLogger.writeLineToFile("Database - Created trades table");
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		try {
			if(!tableNames.contains("items")) {
				Statement statement = connect.createStatement();
				statement.executeQuery("select 1 from " + databaseName + ".items limit 1;");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				createItemsTable();
				documentLogger.writeLineToFile("Database - Created items table");
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		return true;

	}

	/**
	 * USERS: USERID(Primary key) String, EyeLevel int, , data string
	 * TRADES: TRADEID(Primary key), TraderOne(Secondary key), TraderTwo(secondary Key), ItemsOne string, ItemsTwo string, tradecalculated boolean, tradeMedianValueDifference int, tradeMinimumValueDifference int, tradeMaximumValueDifference int, tradeWarningLevel int, timeStampCalculated string, checksum String, data string;
	 * Items: ItemId, Minimum, Medium, Maximum,  , data string
	 */

	private boolean createUsersTable() throws SQLException {
		PreparedStatement statement = prepareSafeStatement(
				"create table users(USER_ID VARCHAR(30), EYE_LEVEL int not null default 1, ADD_DATA VARCHAR, PRIMARY KEY (USER_ID))");
		return statement.execute();
	}

	private boolean createTradesTable() throws SQLException {
		PreparedStatement statement = prepareSafeStatement(
				"create table trades(TRADE_ID VARCHAR(30), TRADER_ONE_ID VARCHAR(30), TRADER_TWO_ID VARCHAR(30), ITEMS_ONE VARCHAR, ITEMS_TWO VARCHAR, TRADE_CALCULATED VARCHAR, TRADE_MIN_DIFF int not null default 0, TRADE_MED_DIFF int not null default 0, TRADE_MAX_DIFF int not null default 0, TRADE_WARNING_LEVEL int not null default 0, TIME_STAMP VARCHAR, CHECK_SUM VARCHAR, ADD_DATA VARCHAR, PRIMARY KEY (TRADE_ID), FOREIGN KEY(TRADER_ONE_ID) REFERENCES users(USER_ID), FOREIGN KEY(TRADER_TWO_ID) REFERENCES users(USER_ID))");
		return statement.execute();
	}

	private boolean createItemsTable() throws SQLException {
		PreparedStatement statement = prepareSafeStatement(
				"create table items(ITEM_ID VARCHAR(30), MINIMUM FLOAT not null default 0, MEDIAN FLOAT not null default 0, MAXIMUM FLOAT not null default 0, RECENT_TRADE_VALUES INT ARRAY[50], TOTAL_TRADES INT not null default 0, CERTAINTY_PERCENTAGE INT not null default 0, PRIMARY KEY(ITEM_ID))");
		return statement.execute();
	}

	public Connection getConnection() {
		return connect;
	}

	public String getDatabaseName() {
		return databaseName;
	}


	public PreparedStatement prepareSafeStatement(String val) throws SQLException {
		timeOnLastQuery = System.currentTimeMillis();
		try {
			PreparedStatement statement = connect.prepareStatement(val);
			return statement;
		} catch (SQLNonTransientConnectionException e) {
			System.err.println("databaseConnection is stale, trying to make a new connection");
			documentLogger.writeLineToFile("databaseConnection is stale, trying to make a new connection");
			try {
				connect = DriverManager.getConnection(databaseUrl, databaseUsername, databasePassword);
				documentLogger.writeLineToFile("success in making a new database connection");
				return connect.prepareStatement(val);
			} catch (SQLException e1) {
				System.err.println("FATAL ERROR; couldn't etablish a new connection from stale");
				documentLogger.writeLineToFile("FATAL ERROR; couldn't etablish a new connection from stale");
				e1.printStackTrace();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		throw new SQLException("Failure to make a safe statement");
	}
}
