package db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import data_retrievers.ITradeDataRetriever;
import dto.Trade;
import dto.TradeItem;
import dto.TradeResult;
import dto.TradeResult.TradeCalculated;
import util.DataTypeHelper;

public class DatabaseTradeDataRetriever implements ITradeDataRetriever {

	private final String tableName;
	
	private DatabaseController dbController;
	
	public DatabaseTradeDataRetriever(DatabaseController dbController) {
		this.dbController = dbController;
		this.tableName = "trades";
	}
	
	@Override
	public List<Trade> getAllTrades() throws SQLException {
		PreparedStatement statement = dbController.prepareSafeStatement("select * from " + dbController.getDatabaseName() + "."+tableName);
		ResultSet set = statement.executeQuery();
		List<Trade> trades = new ArrayList<>();
		while(set.next()) {
			trades.add(setToTrade(set));
		}
		return trades;
	}

	@Override
	public List<Trade> getTradesOfUser(String trader) throws SQLException {
		PreparedStatement statement = dbController.prepareSafeStatement("select * from " + dbController.getDatabaseName() + "."+tableName+" where TRADER_ONE = ? OR TRADER_TWO = ?");
		statement.setString(1, trader);
		statement.setString(2, trader);
		ResultSet set = statement.executeQuery();
		List<Trade> trades = new ArrayList<>();
		while(set.next()) {
			trades.add(setToTrade(set));
		}
		return trades;
	}

	@Override
	public List<Trade> getTradesOfUserWithUser(String trader, String traderTwo) throws SQLException {
		PreparedStatement statement = dbController.prepareSafeStatement("select * from " + dbController.getDatabaseName() + "."+tableName+" where (TRADER_ONE = ? AND TRADER_TWO = ?) OR (TRADER_ONE = ? AND TRADER_TWO = ?)");
		statement.setString(1, trader);
		statement.setString(2, traderTwo);
		statement.setString(3, traderTwo);
		statement.setString(4, trader);
		ResultSet set = statement.executeQuery();
		List<Trade> trades = new ArrayList<>();
		while(set.next()) {
			trades.add(setToTrade(set));
		}
		return trades;
	}

	@Override
	public Trade getTrade(String tradeId) throws Exception {
		PreparedStatement statement = dbController.prepareSafeStatement("select 1 from " + dbController.getDatabaseName() + "."+tableName+" where TRADE_ID = ?");
		statement.setString(1, tradeId);
		ResultSet set = statement.executeQuery();
		if(set.next()) {
			return setToTrade(set);
		}
		return null;
	}

	@Override
	public boolean addTrade(String tradeId, String traderOne, String traderTwo, List<TradeItem> itemsOne,
			List<TradeItem> itemsTwo, TradeResult tradeResult) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateTradeResult(String tradeId, TradeResult tradeResult) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean tradeExists(String tradeId) throws Exception {
		PreparedStatement statement = dbController.prepareSafeStatement("select 1 from " + dbController.getDatabaseName() + "."+tableName+" where TRADE_ID = ?");
		statement.setString(1, tradeId);
		ResultSet set = statement.executeQuery();
		if(set.next()) {
			return true;
		}
		return false;
	}
	
	private Trade setToTrade(ResultSet set) throws SQLException {
		Trade trade = new Trade();
		trade.setTradeId(set.getString(1));
		trade.setTraderOne(set.getString(2));
		trade.setTraderTwo(set.getString(3));
		trade.setItemsOne(DataTypeHelper.stringToTradeItemList(set.getString(4)));
		trade.setItemsTwo(DataTypeHelper.stringToTradeItemList(set.getString(5)));
		TradeResult tResult = new TradeResult();
		tResult.setTradeCalculated(TradeCalculated.valueOf(set.getString(6)));
		tResult.setTradeMinimumValueDifference(set.getInt(7));
		tResult.setTradeMedianValueDifference(set.getInt(8));
		tResult.setTradeMaximumValueDifference(set.getInt(9));
		tResult.setTradeWarningLevel(set.getInt(10));
		tResult.setTimeStampCalculated(set.getString(11));
		tResult.setChecksum(set.getString(12));
		trade.setTradeResult(tResult);
		return trade;
	}

}
