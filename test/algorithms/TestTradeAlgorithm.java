package algorithms;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import controllers.ItemDataController;
import controllers.UserController;
import data.ItemData;
import data_retrievers.IItemDataRetriever;
import dto.Trade;
import dto.TradeItem;
import dto.TradeResult.TradeCalculated;
import extensions.TestItemDataRetriever;
import extensions.TestUserDataRetriever;
import util.ValueUtil;

public class TestTradeAlgorithm {

	private ItemDataController itemDataController;
	private IItemDataRetriever idr;
	private UserController userController;

	@BeforeEach
	void setup() {
		idr = new TestItemDataRetriever();
		itemDataController = new ItemDataController(idr);
		userController = new UserController(null, itemDataController, new TestUserDataRetriever(), null);
	}

	private Trade performStandardTrade(int quantityOne, int quantityTwo) {
		Trade trade = new Trade();
		trade.setTradeId("abc");
		trade.setTraderOne("TraderOne");
		trade.setTraderTwo("TraderTwo");
		List<TradeItem> itemsOne = new ArrayList<>();
		List<TradeItem> itemsTwo = new ArrayList<>();
		itemsOne.add(new TradeItem("itemOne", quantityOne));
		itemsTwo.add(new TradeItem("itemTwo", quantityTwo));
		trade.setItemsOne(itemsOne);
		trade.setItemsTwo(itemsTwo);
		Trade returnedTrade = TradeAlgorithms.processTrade(trade, itemDataController, userController, true);
		return returnedTrade;
	}

	@Test
	void testTradeProcessedFirstTradeItems() throws Exception {
		Trade returnedTrade = performStandardTrade(1,2);
		assertTrue(returnedTrade != null);
		assertTrue(returnedTrade.getTradeResult().getTradeCalculated() != TradeCalculated.NOT_COMPLETED);
		ItemData dataTwo = idr.getItem("itemTwo");
		assertTrue(dataTwo != null);
		assertEquals(50f, dataTwo.getEstimatedPrice().getMedianPrice());

		ItemData dataOne = idr.getItem("itemOne");
		assertTrue(dataOne != null);
		assertEquals(100f, dataOne.getEstimatedPrice().getMedianPrice());
	}

	@Test
	void testTradeProcessedTwiceTradeItems() throws Exception {
		performStandardTrade(1,2);
		Trade returnedTrade = performStandardTrade(1,2);
		assertTrue(returnedTrade.getTradeResult().getTradeCalculated() != TradeCalculated.NOT_COMPLETED);
		ItemData dataTwo = idr.getItem("itemTwo");
		assertTrue(dataTwo != null);
		assertEquals(50f, dataTwo.getEstimatedPrice().getMedianPrice());

		ItemData dataOne = idr.getItem("itemOne");
		assertTrue(dataOne != null);
		assertEquals(100f, dataOne.getEstimatedPrice().getMedianPrice());
	}

	@Test
	void testTradeProcessedMultipleTradeItems() throws Exception {
		for(int i = 0; i < 10; i++) {
			performStandardTrade(1,2);
		}
		Trade returnedTrade = performStandardTrade(1,2);
		assertTrue(returnedTrade.getTradeResult().getTradeCalculated() != TradeCalculated.NOT_COMPLETED);
		ItemData dataTwo = idr.getItem("itemTwo");
		assertTrue(dataTwo != null);
		assertEquals(50f, dataTwo.getEstimatedPrice().getMedianPrice());

		ItemData dataOne = idr.getItem("itemOne");
		assertTrue(dataOne != null);
		assertEquals(100f, dataOne.getEstimatedPrice().getMedianPrice());
	}

	@Test
	void testTradeProcessedAdjustingPrice() throws Exception {
		Random rand = new Random();
		performStandardTrade(1, 2);
		rand.setSeed(System.currentTimeMillis());
		for(int i = 0; i < 100; i++) {
			
			int quantityOne = rand.nextInt(100) + 1;
			int quantityTwo = rand.nextInt(5800) + 500;
			performStandardTrade(quantityOne,quantityTwo);
		}
		//Perform different value trading
		for(int i = 0; i < 100; i++) {
			performStandardTrade(1, 3);
		}
		ItemData dataTwo = idr.getItem("itemTwo");
		ItemData dataOne = idr.getItem("itemOne");
		assertTrue(dataTwo != null);
		assertTrue(ValueUtil.isAroundEqual(dataOne.getEstimatedPrice().getMedianPrice(), dataTwo.getEstimatedPrice().getMedianPrice()*3f, 0.5f));

	}

}
