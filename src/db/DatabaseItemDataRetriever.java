package db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import data.ItemData;
import data.PricePoint;
import data_retrievers.IItemDataRetriever;
import dto.Percentage;
import util.DataTypeHelper;

public class DatabaseItemDataRetriever implements IItemDataRetriever{

	private final String tableName;
	
	private DatabaseController dbController;
	
	public DatabaseItemDataRetriever(DatabaseController dbController) {
		this.dbController = dbController;
		this.tableName = "items";
	}
	
	@Override
	public ItemData getItem(String itemId) throws Exception {
		PreparedStatement statement = dbController.prepareSafeStatement("select * from " + dbController.getDatabaseName() + "."+tableName+" where ITEM_ID = ?");
		statement.setString(1, itemId);
		ResultSet itemData = statement.executeQuery();
		if(itemData.next()) {
			ItemData item = new ItemData();
			item.setItemId(itemId);
			item.setItemValueCertaintyPercentage(new Percentage(itemData.getInt(7)));
			item.setRecentTradeValues(DataTypeHelper.sqlArrayToIntArray(itemData.getArray(5)));
			item.setTotalTrades(itemData.getInt(6));
			item.setEstimatedPrice(new PricePoint(itemData.getFloat(2), itemData.getFloat(3), itemData.getFloat(4)));
			return item;
		}
		return null;
	}

	@Override
	public boolean updatePrice(String itemId, PricePoint pricePoint) throws Exception {
		if(itemExists(itemId)) {
			PreparedStatement statementUpdate = dbController.prepareSafeStatement("update " + dbController.getDatabaseName() + "."+tableName+" set MINIMUM = ?, MEDIAN = ?, MAXIMUM = ? where ITEM_ID = ?");
			statementUpdate.setFloat(1, pricePoint.getMinimumPrice());
			statementUpdate.setFloat(2, pricePoint.getMedianPrice());
			statementUpdate.setFloat(3, pricePoint.getMaximumPrice());
			statementUpdate.setString(4, itemId);
			int res = statementUpdate.executeUpdate();
			if(res != 0) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean updateRecentTrades(String itemId, Integer[] recentTrades) throws Exception {
		if(itemExists(itemId)) {
			PreparedStatement statementUpdate = dbController.prepareSafeStatement("update " + dbController.getDatabaseName() + "."+tableName+" set RECENT_TRADE_VALUES = ? where ITEM_ID = ?");
			statementUpdate.setArray(1, DataTypeHelper.intArrayToSqlArray(recentTrades, dbController.getConnection()));
			statementUpdate.setString(2, itemId);
			int res = statementUpdate.executeUpdate();
			if(res != 0) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean updateTotalTrades(String itemId, int totalTrades) throws Exception {
		if(itemExists(itemId)) {
			PreparedStatement statementUpdate = dbController.prepareSafeStatement("update " + dbController.getDatabaseName() + "."+tableName+" set TOTAL_TRADES = ? where ITEM_ID = ?");
			statementUpdate.setInt(1, totalTrades);
			statementUpdate.setString(2, itemId);
			int res = statementUpdate.executeUpdate();
			if(res != 0) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean updateCertaintyPercentage(String itemId, int certaintyPercentage) throws Exception {
		if(itemExists(itemId)) {
			PreparedStatement statementUpdate = dbController.prepareSafeStatement("update " + dbController.getDatabaseName() + "."+tableName+" set CERTAINTY_PERCENTAGE = ? where ITEM_ID = ?");
			statementUpdate.setInt(1, certaintyPercentage);
			statementUpdate.setString(2, itemId);
			int res = statementUpdate.executeUpdate();
			if(res != 0) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean addItem(String itemId, PricePoint pricePoint, Integer[] recentTrades, int totalTrades,
			int certaintyPercentage) throws Exception {
		if(itemExists(itemId)) {
			return false;
		}
		PreparedStatement statement = dbController.prepareSafeStatement("insert into " + dbController.getDatabaseName() + "."+tableName+" (ITEM_ID, MINIMUM, MEDIAN, MAXIMUM, RECENT_TRADE_VALUES, TOTAL_TRADES, CERTAINTY_PERCENTAGE) values(?,?,?,?,?,?,?)");
		statement.setString(1, itemId);
		statement.setFloat(2, pricePoint.getMinimumPrice());
		statement.setFloat(3, pricePoint.getMedianPrice());
		statement.setFloat(4, pricePoint.getMaximumPrice());
		statement.setArray(5, DataTypeHelper.intArrayToSqlArray(recentTrades, dbController.getConnection()));
		statement.setInt(6, totalTrades);
		statement.setInt(7, certaintyPercentage);
		dbController.getConnection().setAutoCommit(false);
		statement.executeUpdate();
		dbController.getConnection().commit();
		dbController.getConnection().setAutoCommit(true);
		return true;
	}

	@Override
	public boolean itemExists(String itemId) throws Exception {
		PreparedStatement statement;
		statement = dbController.prepareSafeStatement(
				"select * from " + dbController.getDatabaseName() + "."+tableName+" where ITEM_ID = ?");
		statement.setString(1, itemId);
		ResultSet set = statement.executeQuery();
		if (set.next()) {
			return true;
		}
		return false;
	}

}
