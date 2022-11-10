package controllers;

import java.util.List;

import data.UserTradeGraph;
import data_retrievers.ITradeDataRetriever;
import dto.Trade;

public class TradeController {

	private ITradeDataRetriever tradeDataRetriever;
	
	public TradeController(ITradeDataRetriever tdr) {
		this.tradeDataRetriever = tdr;
	}
	
	public List<Trade> getAllTrades(){
		return tradeDataRetriever.getAllTrades();
	}
	
	public List<Trade> getTradesOfUser(String trader, UserTradeGraph.DetailLevel detailLevel){
		return tradeDataRetriever.getTradesOfUser(trader);
	}
	
	public List<Trade> getTradesOfUserWithUser(String trader, String traderTwo, UserTradeGraph.DetailLevel detailLevel){
		return tradeDataRetriever.getTradesOfUserWithUser(trader, traderTwo);
	}

	public ITradeDataRetriever getTradeDataRetriever() {
		return tradeDataRetriever;
	}
	
}
