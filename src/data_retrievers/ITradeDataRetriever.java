package data_retrievers;

import java.util.List;

import dto.Trade;
import dto.TradeItem;
import dto.TradeResult;

public interface ITradeDataRetriever {

	public List<Trade> getAllTrades() throws Exception;
	
	public List<Trade> getTradesOfUser(String trader) throws Exception;
	
	public List<Trade> getTradesOfUserWithUser(String trader, String traderTwo) throws Exception;
	
	public Trade getTrade(String tradeId) throws Exception;
	
	public boolean addTrade(String tradeId, String traderOne, String traderTwo, List<TradeItem> itemsOne, List<TradeItem> itemsTwo, TradeResult tradeResult) throws Exception;
	
	public boolean updateTradeResult(String tradeId, TradeResult tradeResult) throws Exception;
	
	public boolean tradeExists(String tradeId) throws Exception;
	
}
