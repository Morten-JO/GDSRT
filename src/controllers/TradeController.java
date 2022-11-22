package controllers;

import java.util.ArrayList;
import java.util.List;

import algorithms.TradeAlgorithms;
import data.UserTradeGraph;
import data_retrievers.ITradeDataRetriever;
import dto.Trade;

public class TradeController {

	private TradeAlgorithms tradeAlgorithms;
	private ITradeDataRetriever tradeDataRetriever;
	
	public TradeController(ITradeDataRetriever tdr) {
		this.tradeDataRetriever = tdr;
		this.tradeAlgorithms = new TradeAlgorithms();
	}
	
	public boolean addTrade(Trade trade, ItemDataController itemDataController, UserController userController) {
		Trade processedTrade = tradeAlgorithms.processTrade(trade, itemDataController, userController);
		try {
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
