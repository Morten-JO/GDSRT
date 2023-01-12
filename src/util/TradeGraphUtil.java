package util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import data.UserTradeGraph;
import dto.Trade;

public class TradeGraphUtil {

	public static String tradeGraphToString(UserTradeGraph graph, int warningLevelThreshold, int indentation) {
		if(graph.getWarningLevel() < warningLevelThreshold) {
			return "{IGNORED}";
		}
		String str = "";
		if(indentation != 0) {
			str += System.lineSeparator();
		}
		str += indentationToSpaces(indentation)+"{"+System.lineSeparator()+indentationToSpaces(indentation)+"\"layer\": "+graph.getLayers()+", \"warning\": "+graph.getWarningLevel()+", ";
		str += "\""+graph.getUserId()+"\": {";
		str += "\"trades\": [";
		for(Trade trade : graph.getTrades()) {
			str += "{";
			str += "\"trade\": \""+trade.getTradeId()+"\", \"warning_level\": "+trade.getTradeResult().getTradeWarningLevel()+", \"trade_diff\": "+trade.getTradeResult().getTradeMedianValueDifference();
			str += "}";
			if(trade != graph.getTrades().get(graph.getTrades().size()-1)) {
				str += ",";
			}
		}
		str += "], \"child_graphs\": [";
		for(UserTradeGraph lowerGraph : graph.getPoints()) {
			str += tradeGraphToString(lowerGraph, warningLevelThreshold, indentation+2);
			if(lowerGraph != graph.getPoints().get(graph.getPoints().size()-1)) {
				str += ",";
			}
		}
		str += "]}"+System.lineSeparator()+indentationToSpaces(indentation)+"}";
		return str;
	}
	
	private static String indentationToSpaces(int indentation) {
		String space = " ";
		return IntStream.range(0, indentation).mapToObj(i -> space).collect(Collectors.joining(""));
	}
	
	public static boolean writeDataReportOnGraph(UserTradeGraph graph, int warningLevelThreshold) throws IOException {
		return true;
	}
	
	public static boolean writeUserGraphFile(UserTradeGraph graph, int warningLevelThreshold) throws IOException {
		String toWrite = tradeGraphToString(graph, warningLevelThreshold, 0);

		PrintWriter out;
		FileWriter writer;
		BufferedWriter bufferedWriter;
		ArrayList<String> storedLines = new ArrayList<>();
		File file = new File("UserTradeGraph_"+System.currentTimeMillis()+".json");
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
