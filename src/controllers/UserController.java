package controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import data.UserTradeGraph;
import data_retrievers.IUserDataRetriever;
import dto.Trade;
import user.User;

public class UserController extends TimerTask{

	private Map<String, Long> usersRequestedForCheck = new HashMap<String, Long>();
	private static Long MIN_DURATION_FOR_CHECK = 5000L;
	private static Long DURATION_PER_CHECK = 60000L;
	private TradeController tradeController;
	private IUserDataRetriever userDataRetriever;
	
	public UserController(TradeController tc, IUserDataRetriever userDataRetriever, boolean userChecksEnabled) {
		this.tradeController = tc;
		this.userDataRetriever = userDataRetriever;
		if(userChecksEnabled) {
			Timer t = new Timer();
			t.scheduleAtFixedRate(this, DURATION_PER_CHECK, DURATION_PER_CHECK);
		}
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
			if(runRequestedCheckOnUser(entry.getKey())) {
				usersRequestedForCheck.remove(entry.getKey());
			} else {
				usersRequestedForCheck.put(entry.getKey(), entry.getValue() + MIN_DURATION_FOR_CHECK);
			}
		}
	}
	
	/**
	 * Logic for running the requestedCheck
	 * @param userId
	 * @return false if check is not successfull, true if it was
	 */
	private boolean runRequestedCheckOnUser(String userId) {
		//Third(IF), if first is found to be "sketch", then look into trade history of individuals, if none or little wrong, dont do anything
		//else increase eyeLevel
		//Should eyeLevel be above certain threshold, send warning maybe? Maybe it shouldn't be here
		return false;
	}
	
	public UserTradeGraph retrieveGraphForUser(String user, int layers, UserTradeGraph.DetailLevel detailLevel) {
		UserTradeGraph graph = new UserTradeGraph();
		graph.setUserId(user);
		graph.setLayers(layers);
		List<Trade> tradesOfUser = tradeController.getTradesOfUser(user, detailLevel);
		List<UserTradeGraph> tradePoints = new ArrayList<>();
		List<String> users = getListOfUsersFromTrades(tradesOfUser, user);
		for(String u : users) {
			User childUser = getUser(u);
			if(childUser == null) {
				continue;
			}
			UserTradeGraph childGraph = new UserTradeGraph();
			childGraph.setOwner(graph);
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
		if(trades.size() == 0) {
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
	
	public TradeController getTradeController() {
		return tradeController;
	}
	
	public IUserDataRetriever getUserDataRetriever() {
		return userDataRetriever;
	}
	
}
