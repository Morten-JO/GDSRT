package controllers;

import java.util.ArrayList;
import java.util.List;

import algorithms.TradeAlgorithms;
import data.UserTradeGraph;
import data_retrievers.ITradeDataRetriever;
import dto.Trade;
import util.DataTypeGenerator;

public class TradeController {

	private ITradeDataRetriever tradeDataRetriever;
	
	public TradeController(ITradeDataRetriever tdr) {
		this.tradeDataRetriever = tdr;
	}
	
	public boolean addTrade(Trade trade, ItemDataController itemDataController, UserController userController) {
		Trade processedTrade = TradeAlgorithms.processTrade(trade, itemDataController, userController, true);
		try {
			if(processedTrade.getTradeId() == null) {
				processedTrade.setTradeId(DataTypeGenerator.generatedTradeId());
			}
			tradeDataRetriever.addTrade(processedTrade.getTradeId(), processedTrade.getTraderOne(), processedTrade.getTraderTwo(), processedTrade.getItemsOne(), processedTrade.getItemsTwo(), processedTrade.getTradeResult());
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public List<Trade> getAllTrades(){
		try {
			return tradeDataRetriever.getAllTrades();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<>();
	}
	
	public List<Trade> getTradesOfUser(String trader, UserTradeGraph.DetailLevel detailLevel){
		try {
			return tradeDataRetriever.getTradesOfUser(trader);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<>();
	}
	
	public List<Trade> getTradesOfUserWithUser(String trader, String traderTwo, UserTradeGraph.DetailLevel detailLevel){
		try {
			return tradeDataRetriever.getTradesOfUserWithUser(trader, traderTwo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<>();
	}

	public ITradeDataRetriever getTradeDataRetriever() {
		return tradeDataRetriever;
	}
	
}
