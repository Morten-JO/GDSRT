package algorithms;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import controllers.ItemDataController;
import controllers.TradeController;
import controllers.UserController;
import data.UserTradeGraph;
import data.UserTradeGraph.DetailLevel;
import dto.Trade;
import dto.TradeItem;
import dto.TradeResult;
import extensions.TestItemDataRetriever;
import extensions.TestTradeDataRetriever;
import extensions.TestUserDataRetriever;
import user.User;

class TestWebgraph {

	private UserController controller;
	
	private static String TRADER_NAME_A = "A";
	private static String TRADER_NAME_B = "B";
	private static String TRADER_NAME_C = "C";
	private static String TRADER_NAME_D = "D";
	
	private Trade getTrade(String traderOne, String traderTwo) {
		Trade trade = new Trade();
		trade.setTraderOne(traderOne);
		trade.setTraderTwo(traderTwo);
		trade.setTradeId("0");
		TradeResult tradeResult = new TradeResult();
		trade.setTradeResult(tradeResult);
		trade.setItemsOne(new ArrayList<TradeItem>());
		trade.setItemsTwo(new ArrayList<TradeItem>());
		return trade;
	}
	
	@BeforeEach
	void setup() {
		controller = new UserController(new TradeController(new TestTradeDataRetriever()), new ItemDataController(new TestItemDataRetriever()), new TestUserDataRetriever());
		User a = new User(TRADER_NAME_A, 0, null);
		User b = new User(TRADER_NAME_B, 0, null);
		User c = new User(TRADER_NAME_C, 0, null);
		User d = new User(TRADER_NAME_D, 0, null);
		((TestUserDataRetriever)controller.getUserDataRetriever()).users.add(a);
		((TestUserDataRetriever)controller.getUserDataRetriever()).users.add(b);
		((TestUserDataRetriever)controller.getUserDataRetriever()).users.add(c);
		((TestUserDataRetriever)controller.getUserDataRetriever()).users.add(d);
	}
	
	@Test
	void testWebGraphEmpty() {
		UserTradeGraph graph = controller.retrieveGraphForUser(TRADER_NAME_A, 4, DetailLevel.ALL);
		assertTrue(graph.getTrades().size() == controller.getTradeController().getTradesOfUser(TRADER_NAME_A, DetailLevel.ALL).size());;
		assertTrue(graph.getTrades().size() == 0);
		assertEquals(graph.getOwner(), null);
	}
	
	@Test
	void testWebGraphMainWeb() {
		List<Trade> trades = new ArrayList<>();
		trades.add(getTrade(TRADER_NAME_A, TRADER_NAME_B));
		trades.add(getTrade(TRADER_NAME_A,TRADER_NAME_C));
		trades.add(getTrade(TRADER_NAME_A, TRADER_NAME_D));
		trades.add(getTrade(TRADER_NAME_B, TRADER_NAME_C));
		((TestTradeDataRetriever)controller.getTradeController().getTradeDataRetriever()).toReturn = trades;
		UserTradeGraph graph = controller.retrieveGraphForUser(TRADER_NAME_A, 4, DetailLevel.ALL);
		assertTrue(graph.getTrades().size() == 3);
		assertTrue(graph.getTrades().size() == controller.getTradeController().getTradesOfUser(TRADER_NAME_A, DetailLevel.ALL).size());
		assertTrue(controller.getTradeController().getAllTrades().size() == trades.size());
		assertEquals(graph.getOwner(), null);
	}
	
	@Test
	void testWebGraphWithSeveralWebs() {
		List<Trade> trades = new ArrayList<>();
		trades.add(getTrade(TRADER_NAME_A, TRADER_NAME_B));
		trades.add(getTrade(TRADER_NAME_B,TRADER_NAME_C));
		trades.add(getTrade(TRADER_NAME_C, TRADER_NAME_D));
		trades.add(getTrade(TRADER_NAME_B, TRADER_NAME_D));
		((TestUserDataRetriever)controller.getUserDataRetriever()).users.get(1).setCurrentAggroLevel(4);;
		((TestTradeDataRetriever)controller.getTradeController().getTradeDataRetriever()).toReturn = trades;
		UserTradeGraph graph = controller.retrieveGraphForUser(TRADER_NAME_A, 4, DetailLevel.ALL);
		assertTrue(graph.getTrades().size() == 1);
		assertTrue(graph.getPoints().size() == 1);
		assertTrue(graph.getPoints().get(0).getUserId().equals(TRADER_NAME_B));
		assertTrue(graph.getPoints().get(0).getOwner() == graph);
		UserTradeGraph graphB = controller.lookAtPointFromUserTradeGraph(graph.getPoints().get(0), TRADER_NAME_B, DetailLevel.ALL);
		assertTrue(graphB.getTrades().size() == 3);
		assertTrue(graphB.getPoints().size() == 3);
		assertTrue(graphB.getTradesWithOwner().size() == 1);
		assertTrue(graphB.getOwner() == graph);
		assertTrue(graphB.getWarningLevel() == 4);
		UserTradeGraph graphC = controller.lookAtPointFromUserTradeGraph(graphB.getPoints().get(0), TRADER_NAME_C, DetailLevel.ALL);
		assertTrue(graphC.getTrades().size() == 2);
		assertTrue(graphC.getPoints().size() == 2);
		assertTrue(graphC.getTradesWithOwner().size() == 1);
		assertTrue(graphC.getOwner() == graphB);
		
		
	}
	
	
}
