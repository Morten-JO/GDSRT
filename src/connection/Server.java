package connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import config.LoadedConfigs;
import controllers.ItemDataController;
import controllers.TradeController;
import controllers.UserController;
import dto.Trade;
import util.DataTypeHelper;
import util.DebugDocumentLogger;
import util.EncryptionHelper;
import util.PrintDebugDocumentLogger;



public class Server {

	private boolean serverRunning = false;
	private boolean acceptingPlayers = false;

	private ServerSocket socket;
	public int incomingServerPort;

	private DebugDocumentLogger logger;

	private TradeController tc;
	private UserController uc;
	private ItemDataController idc;

	private PrivateKey decryptionKey;
	
	private List<Client> clients = new ArrayList<>();

	public Server(TradeController tc, UserController uc, ItemDataController idc) throws IOException {
		incomingServerPort = LoadedConfigs.INCOMING_SERVER_PORT;
		socket = new ServerSocket(incomingServerPort);
		logger = new PrintDebugDocumentLogger();
		this.tc = tc;
		this.uc = uc;
		this.idc = idc;
	}

	public Server(TradeController tc, UserController uc, ItemDataController idc, DebugDocumentLogger logger) throws IOException {
		incomingServerPort = LoadedConfigs.INCOMING_SERVER_PORT;
		socket = new ServerSocket(incomingServerPort);
		this.logger = logger;
		this.tc = tc;
		this.uc = uc;
		this.idc = idc;
	}

	public void run(){
		logger.writeLineToFile("Server started.");
		serverRunning = true;
		setAcceptingPlayersState(true);
		while (serverRunning) {
			if (acceptingPlayers) {
				try {
					Socket clientSocket = socket.accept();
					logger.writeLineToFile("Accepted a tcp user: " + clientSocket.getInetAddress().getHostAddress());
					BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
					PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
					Client client = new Client(this, clientSocket, writer, reader);
					client.getConnection().startConnection();
					clients.add(client);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void setAcceptingPlayersState(boolean state) {
		acceptingPlayers = state;
	}

	public void requestCloseClient(Client client) {
		if (client.getConnection() != null) {
			client.removeConnection();
		}
	}

	public void newTradeMessage(String msg) {
		logger.writeLineToFile("New trade message received.");
		String msgToProcess = msg;
		if(LoadedConfigs.ENCRYPTION) {
			try {
				msgToProcess = EncryptionHelper.decryptMsgWithPrivateKey(msg, decryptionKey);
			} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException
					| BadPaddingException e) {
				e.printStackTrace();
				logger.writeLineToFile("Failed to decrypt message. Message was "+msg);
			}
		}
		Trade trade = DataTypeHelper.tradeStringToTrade(msgToProcess);
		boolean res = tc.addTrade(trade, idc, uc);
		logger.writeLineToFile("Added trade from tcp to tradeController, response was "+res);
	}

	public void setDecryptionKey(PrivateKey privateKey) {
		this.decryptionKey = privateKey;
	}

	public UserController getUserController() {
		return uc;
	}

}
