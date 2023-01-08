package temp_db;

import java.util.ArrayList;
import java.util.List;

import data_retrievers.ITradeDataRetriever;
import dto.Trade;
import dto.TradeItem;
import dto.TradeResult;

public class TempDatabaseTradeDataRetriever implements ITradeDataRetriever{

	private List<Trade> trades = new ArrayList<>();
	
	public TempDatabaseTradeDataRetriever() {
		
	}
	
	@Override
	public List<Trade> getAllTrades() throws Exception {
		return trades;
	}

	@Override
	public List<Trade> getTradesOfUser(String trader) throws Exception {
		List<Trade> userTrades = new ArrayList<>();
		for(Trade trade : trades) {
			if(trade.getTraderOne().equals(trader) || trade.getTraderTwo().equals(trader)) {
				userTrades.add(trade);
			}
		}
		return userTrades;
	}

	@Override
	public List<Trade> getTradesOfUserWithUser(String trader, String traderTwo) throws Exception {
		List<Trade> userTrades = new ArrayList<>();
		for(Trade trade : trades) {
			if(trade.getTraderOne().equals(trader) && trade.getTraderTwo().equals(traderTwo) || trade.getTraderOne().equals(traderTwo) && trade.getTraderTwo().equals(trader)) {
				userTrades.add(trade);
			}
		}
		return userTrades;
	}

	@Override
	public Trade getTrade(String tradeId) throws Exception {
		for(Trade trade : trades) {
			if(trade.getTradeId().equals(tradeId)) {
				return trade;
			}
		}
		return null;
	}

	@Override
	public boolean addTrade(String tradeId, String traderOne, String traderTwo, List<TradeItem> itemsOne,
			List<TradeItem> itemsTwo, TradeResult tradeResult) throws Exception {
		Trade trade = new Trade();
		trade.setTradeId(tradeId);
		trade.setTraderOne(traderOne);
		trade.setTraderTwo(traderTwo);
		trade.setItemsOne(itemsOne);
		trade.setItemsTwo(itemsTwo);
		trade.setTradeResult(tradeResult);
		trades.add(trade);
		return true;
	}

	@Override
	public boolean updateTradeResult(String tradeId, TradeResult tradeResult) throws Exception {
		for(Trade trade : trades) {
			if(trade.getTradeId().equals(tradeId)) {
				trade.setTradeResult(tradeResult);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean tradeExists(String tradeId) throws Exception {
		for(Trade trade : trades) {
			if(trade.getTradeId().equals(tradeId)) {
				return true;
			}
		}
		return false;
	}

}
