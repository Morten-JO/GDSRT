package connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import config.LoadedConfigs;
import controllers.ItemDataController;
import controllers.TradeController;
import controllers.UserController;
import util.DebugDocumentLogger;
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
	
	//TODO add logic here for processing received trades
}
