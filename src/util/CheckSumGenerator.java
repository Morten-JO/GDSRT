package util;

import dto.Trade;

public class CheckSumGenerator {

	public static String generateCheckSum(Trade trade) {
		return ""+trade.hashCode();
	}
	
}
