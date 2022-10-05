package algorithms;

import dto.Trade;
import dto.TradeResult;

public class TradeAlgorithms {

	public Trade processTrade(Trade trade) {
		TradeResult result = new TradeResult();
		//First, assign values to each item in trade, if available, then compare/mark possibly inconclusive
		//Second, add/updates values to the itemdata storage
		//Third(IF), if first is found to be "sketch", then look into trade history of individuals, if none or little wrong, dont do anything
		//else increase eyeLevel
		//Should eyeLevel be above certain threshold, send warning maybe? Maybe it shouldn't be here
		//Set result, then return that shit
		
		
		return trade;
	}
	
}
