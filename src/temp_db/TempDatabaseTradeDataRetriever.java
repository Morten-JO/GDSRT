package temp_db;

import java.util.List;

import data_retrievers.ITradeDataRetriever;
import db.DatabaseController;
import dto.Trade;
import dto.TradeItem;
import dto.TradeResult;

public class TempDatabaseTradeDataRetriever implements ITradeDataRetriever{

	public TempDatabaseTradeDataRetriever() {
		
	}
	
	@Override
	public List<Trade> getAllTrades() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Trade> getTradesOfUser(String trader) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Trade> getTradesOfUserWithUser(String trader, String traderTwo) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Trade getTrade(String tradeId) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean addTrade(String tradeId, String traderOne, String traderTwo, List<TradeItem> itemsOne,
			List<TradeItem> itemsTwo, TradeResult tradeResult) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateTradeResult(String tradeId, TradeResult tradeResult) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean tradeExists(String tradeId) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

}
