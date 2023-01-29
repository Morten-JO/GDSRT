package extensions;

import java.util.ArrayList;
import java.util.List;

import data_retrievers.ITradeDataRetriever;
import dto.Trade;
import dto.TradeItem;
import dto.TradeResult;

public class TestTradeDataRetriever implements ITradeDataRetriever {


	public List<Trade> toReturn = new ArrayList<>();

	@Override
	public List<Trade> getAllTrades(){
		return toReturn;
	}

	@Override
	public List<Trade> getTradesOfUser(String trader) {
		List<Trade> isUser = new ArrayList<>();
		for(Trade t : toReturn) {
			if(t.getTraderOne().equals(trader) || t.getTraderTwo().equals(trader)) {
				isUser.add(t);
			}
		}
		return isUser;
	}

	@Override
	public List<Trade> getTradesOfUserWithUser(String trader, String traderTwo) {
		List<Trade> isUser = new ArrayList<>();
		for(Trade t : toReturn) {
			if(t.getTraderOne().equals(trader) && t.getTraderTwo().equals(traderTwo) || t.getTraderOne().equals(traderTwo) && t.getTraderTwo().equals(trader)) {
				isUser.add(t);
			}
		}
		return isUser;
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
	public boolean updateTradeResult(String tradeId, TradeResult tradeResult) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean tradeExists(String tradeId) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

}
