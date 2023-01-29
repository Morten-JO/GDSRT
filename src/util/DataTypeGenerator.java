package util;

import dto.Trade;

public class DataTypeGenerator {

	public static String generateCheckSum(Trade trade) {
		return ""+trade.hashCode();
	}

	public static String generatedTradeId() {
		//From: https://www.geeksforgeeks.org/generate-random-string-of-given-size-in-java/
		// choose a Character random from this String
		String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
		+ "0123456789"
		+ "abcdefghijklmnopqrstuvxyz";

		// create StringBuffer size of AlphaNumericString
		StringBuilder sb = new StringBuilder(25);

		for (int i = 0; i < 25; i++) {

			// generate a random number between
			// 0 to AlphaNumericString variable length
			int index = (int)(AlphaNumericString.length() * Math.random());

			// add Character one by one in end of sb
			sb.append(AlphaNumericString.charAt(index));
			}

		return sb.toString();
	}

}
