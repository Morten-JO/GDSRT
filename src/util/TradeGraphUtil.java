package util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import data.UserTradeGraph;
import dto.Trade;

public class TradeGraphUtil {

	public static String tradeGraphToString(UserTradeGraph graph, int warningLevelThreshold) {
		if(graph.getWarningLevel() < warningLevelThreshold) {
			return "{IGNORED}";
		}
		String str = "{layer: "+graph.getLayers()+", warning: "+graph.getWarningLevel()+", ";
		str += graph.getUserId()+": {";
		str += "trades: {";
		for(Trade trade : graph.getTrades()) {
			str += trade.getTradeId();
			if(trade != graph.getTrades().get(graph.getTrades().size()-1)) {
				str += ",";
			}
		}
		str += "},"+System.lineSeparator()+" graphs: {";
		for(UserTradeGraph lowerGraph : graph.getPoints()) {
			str += tradeGraphToString(lowerGraph, warningLevelThreshold);
			if(lowerGraph != graph.getPoints().get(graph.getPoints().size()-1)) {
				str += ",";
			}
		}
		str += "}}";
		return str;
	}
	
	public static boolean writeUserGraphFile(UserTradeGraph graph, int warningLevelThreshold) throws IOException {
		String toWrite = tradeGraphToString(graph, warningLevelThreshold);


		PrintWriter out;
		FileWriter writer;
		BufferedWriter bufferedWriter;
		ArrayList<String> storedLines = new ArrayList<>();
		File file = new File("UserTradeGraph_"+System.currentTimeMillis()+".txt");
		if(!file.exists()) {
			file.createNewFile();
		}
		writer = new FileWriter(file, true);
		bufferedWriter = new BufferedWriter(writer);
		out = new PrintWriter(bufferedWriter);
		try {
			out.println(toWrite);
		} catch (NullPointerException e) {
			e.printStackTrace();
			storedLines.add(toWrite);
		}
		PrintWriter old = out;
		int storedLinesSize = storedLines.size();
		for (int i = 0; i < storedLinesSize; i++) {
			old.println(storedLines.get(0));
			storedLines.remove(0);
		}
		old.flush();
		try {
			out.close();
			bufferedWriter.close();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		out = null;
		return true;
	}
	
}
