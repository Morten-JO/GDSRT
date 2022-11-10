package data_retrievers;

import java.util.List;

import dto.Trade;

public interface ITradeDataRetriever {

	public List<Trade> getAllTrades();
	
	public List<Trade> getTradesOfUser(String trader);
	
	public List<Trade> getTradesOfUserWithUser(String trader, String traderTwo);
	
}
