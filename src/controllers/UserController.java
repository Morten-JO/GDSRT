package controllers;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import algorithms.TradeAlgorithms;
import data.UserTradeGraph;
import data.UserTradeGraph.DetailLevel;
import data_retrievers.ITradeDataRetriever;
import data_retrievers.IUserDataRetriever;
import dto.Trade;
import dto.TradeResult;
import rules.TradeRules;
import user.User;
import util.DateStamper;
import util.DebugDocumentLogger;

public class UserController extends TimerTask{

	private Map<String, Long> usersRequestedForCheck = new ConcurrentHashMap<>();
	private static Long MIN_DURATION_FOR_CHECK = 5000L;
	private static Long DURATION_PER_CHECK = 60000L;
	private TradeController tradeController;
	private ItemDataController itemDataController;
	private IUserDataRetriever userDataRetriever;
	private ITradeDataRetriever tradeDataRetriever;
	private DebugDocumentLogger logger;

	/**
	 * No userChecksEnabled
	 * @param tc
	 * @param userDataRetriever
	 * @param userChecksEnabled
	 */
	public UserController(TradeController tc, ItemDataController idc, IUserDataRetriever userDataRetriever, DebugDocumentLogger logger) {
		this.tradeController = tc;
		this.itemDataController = idc;
		this.userDataRetriever = userDataRetriever;
		this.logger = logger;
	}

	/**
	 * UserChecksEnabled is true
	 * @param tc
	 * @param userDataRetriever
	 * @param userChecksEnabled
	 */
	public UserController(TradeController tc, IUserDataRetriever userDataRetriever, ITradeDataRetriever tradeDataRetriever, ItemDataController idc, DebugDocumentLogger logger) {
		this.tradeController = tc;
		this.userDataRetriever = userDataRetriever;
		this.tradeDataRetriever = tradeDataRetriever;
		this.itemDataController = idc;
		this.logger = logger;
		Timer t = new Timer();
		t.scheduleAtFixedRate(this, DURATION_PER_CHECK, DURATION_PER_CHECK);
	}



	/**
	 * Requests to check the user
	 * @return true if request is added, false if not.
	 */
	public boolean requestUserCheck(String userId) {
		if(usersRequestedForCheck.containsKey(userId)) {
			return false;
		} else {
			usersRequestedForCheck.put(userId, System.currentTimeMillis());
		}
		return true;
	}

	@Override
	public void run() {
		runRequestedChecks();
	}

	private void runRequestedChecks() {
		for(Map.Entry<String, Long> entry : usersRequestedForCheck.entrySet()) {
			if(entry.getValue() + MIN_DURATION_FOR_CHECK > System.currentTimeMillis()) {
				continue;
			}
			try {
				if(runRequestedCheckOnUser(entry.getKey())) {
					usersRequestedForCheck.remove(entry.getKey());
				} else {
					usersRequestedForCheck.put(entry.getKey(), entry.getValue() + MIN_DURATION_FOR_CHECK);
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.err.println("Failed to perform check... Adding time and doing it again");
				entry.setValue(System.currentTimeMillis());
			}
		}
	}

	/**
	 * Logic for running the requestedCheck
	 * @param userId
	 * @return false if check is not successfull, true if it was
	 * @throws Exception
	 */
	private boolean runRequestedCheckOnUser(String userId) throws Exception {
		User user = userDataRetriever.getUser(userId);
		List<Trade> tradesOfUser = tradeDataRetriever.getTradesOfUser(userId);
		List<Trade> newTradesProcessed = new ArrayList<>();
		int oldWarningLevel = 0;
		int newWarningLevel = 0;
		for(Trade trade: tradesOfUser) {
			if(trade.getTradeResult().getTradeWarningLevel() == 0 && trade.getTradeResult().getTradeCalculated() == TradeResult.TradeCalculated.FINALIZED) {
				continue;
			}
			LocalTime tradeDate = DateStamper.returnStampedDate(trade.getTradeResult().getTimeStampCalculated());
			LocalTime currentTime = LocalTime.now();

			if(Duration.between(currentTime, tradeDate).toDays() >= TradeRules.DAYS_FOR_TRADE_TO_EXPIRE) {
				continue;
			}
			Trade copiedTrade = new Trade(trade);
			Trade processedTrade = TradeAlgorithms.processTrade(copiedTrade, itemDataController, this, false);
			if(processedTrade.getTradeResult().getTradeWarningLevel() == trade.getTradeResult().getTradeWarningLevel() && processedTrade.getTradeResult().getTradeCalculated() == trade.getTradeResult().getTradeCalculated()) {
				continue;
			}

			newTradesProcessed.add(processedTrade);
			oldWarningLevel += trade.getTradeResult().getTradeWarningLevel();
			newWarningLevel += processedTrade.getTradeResult().getTradeWarningLevel();
		}
		if(Math.abs(newWarningLevel - oldWarningLevel) > oldWarningLevel / 10f) {
			if(logger != null)
				logger.writeLineToFile("User: "+userId+" warning level was increased");
			user.setCurrentAggroLevel(user.getCurrentAggroLevel() + 1);
			for(Trade trade : newTradesProcessed) {
				tradeDataRetriever.updateTradeResult(trade.getTradeId(), trade.getTradeResult());
			}
			userDataRetriever.updateUserEyeLevel(userId, user.getCurrentAggroLevel());
			return true;
		}
		return true;
	}

	public UserTradeGraph retrieveGraphForUser(String user, int layers, int warning, UserTradeGraph.DetailLevel detailLevel) {
		UserTradeGraph graph = retrieveGraphForUserWithWarning(user, layers, warning, new ArrayList<String>(), null, null);
		return graph;
	}

	private UserTradeGraph retrieveGraphForUserWithWarning(String user, int layers, int warning, List<String> traversed, UserTradeGraph owner, List<Trade> childTradesWithOwner) {
		if(traversed.contains(user)) {
			return null;
		} else {
			traversed.add(user);
		}
		UserTradeGraph graph = new UserTradeGraph();
		graph.setUserId(user);
		graph.setLayers(layers);
		graph.setWarningLevel(warning);
		graph.setOwner(owner);
		graph.setTradesWithOwner(childTradesWithOwner);

		List<Trade> tradesOfUser = tradeController.getTradesOfUser(user, DetailLevel.HIGHWARNING);
		List<UserTradeGraph> tradePoints = new ArrayList<>();
		List<String> users = getListOfUsersFromTrades(tradesOfUser, user, warning);
		for(String u : users) {
			User childUser = getUser(u);
			if(childUser == null) {
				continue;
			}
			List<Trade> childTrades = new ArrayList<>();
			for(Trade trade : tradesOfUser) {
				if(trade.getTradeResult().getTradeWarningLevel() >= warning) {
					if(trade.getTraderOne().equals(u) || trade.getTraderTwo().equals(u)) {
						childTrades.add(trade);
					}
				}
			}
			if(layers > 0) {
				UserTradeGraph childGraph = retrieveGraphForUserWithWarning(u, layers-1, warning, traversed, owner, childTrades);
				if(childGraph != null) {
					tradePoints.add(childGraph);
				}
			}
		}
		graph.setTrades(tradesOfUser);
		graph.setPoints(tradePoints);
		return graph;


	}

	public UserTradeGraph lookAtPointFromUserTradeGraph(UserTradeGraph orig, String userId, UserTradeGraph.DetailLevel detailLevel) {
		User user = getUser(userId);
		if(user == null){
			return orig;
		}
		orig.setUserId(user.getUserIdentification());
		orig.setWarningLevel(user.getCurrentAggroLevel());
		List<Trade> tradesOfUser = tradeController.getTradesOfUser(user.getUserIdentification(), detailLevel);
		List<UserTradeGraph> tradePoints = new ArrayList<>();
		List<String> users = getListOfUsersFromTrades(tradesOfUser, user.getUserIdentification());
		for(String u : users) {
			User childUser = getUser(u);
			if(childUser == null) {
				continue;
			}
			UserTradeGraph childGraph = new UserTradeGraph();
			childGraph.setOwner(orig);
			List<Trade> childTradesWithOwner = new ArrayList<>();
			for(Trade trade : tradesOfUser) {
				if(trade.getTraderOne().equals(u) || trade.getTraderTwo().equals(u)) {
					childTradesWithOwner.add(trade);
				}
			}
			childGraph.setTradesWithOwner(childTradesWithOwner);
			childGraph.setUserId(childUser.getUserIdentification());
			childGraph.setWarningLevel(childUser.getCurrentAggroLevel());
			tradePoints.add(childGraph);

		}
		orig.setTrades(tradesOfUser);
		orig.setPoints(tradePoints);
		return orig;
	}

	private User getUser(String userId) {
		try {
			return userDataRetriever.getUser(userId);
		} catch (Exception e) {
			return null;
		}
	}

	private List<String> getListOfUsersFromTrades(List<Trade> trades, String originalTrader){
		List<String> users = new ArrayList<>();
		if((trades == null) || (trades.size() == 0)) {
			return users;
		}
		for(Trade trade: trades) {
			String trader;
			if(trade.getTraderOne().equals(originalTrader)) {
				trader = trade.getTraderTwo();
			} else {
				trader = trade.getTraderOne();
			}
			if(!users.contains(trader)) {
				users.add(trader);
			}
		}
		return users;
	}

	private List<String> getListOfUsersFromTrades(List<Trade> trades, String originalTrader, int warningLevel){
		List<String> users = new ArrayList<>();
		if((trades == null) || (trades.size() == 0)) {
			return users;
		}
		for(Trade trade: trades) {
			if(trade.getTradeResult() == null) {
				continue;
			} else {
				if (trade.getTradeResult().getTradeWarningLevel() < warningLevel) {
					continue;
				}
			}
			String trader;
			if(trade.getTraderOne().equals(originalTrader)) {
				trader = trade.getTraderTwo();
			} else {
				trader = trade.getTraderOne();
			}
			if(!users.contains(trader)) {
				users.add(trader);
			}
		}
		return users;
	}

	public TradeController getTradeController() {
		return tradeController;
	}

	public IUserDataRetriever getUserDataRetriever() {
		return userDataRetriever;
	}

	public boolean userExists(String name) {
		try {
			return userDataRetriever.userExists(name);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean createNewUser(String name) {
		try {
			if(!userDataRetriever.userExists(name)) {
				return userDataRetriever.addUser(name, 0, "{}");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;

	}

}
