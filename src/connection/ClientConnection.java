package connection;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

import data.UserTradeGraph.DetailLevel;
import util.TradeGraphUtil;


public class ClientConnection extends Connection {

	/**
	 * This class is the connection from the Game servers to the Client
	 */

	protected Thread keepAliveThread;
	protected Long intervalKeepAlive = 10000L;
	private Long count;
	protected Server hostServer;
	protected Client connectionOwner;

	public ClientConnection(Client connectionOwner, Server hostServer, Socket socket,
			BufferedReader reader, PrintWriter writer) {
		super(socket, reader, writer);
		this.hostServer = hostServer;
		this.connectionOwner = connectionOwner;
	}

	@Override
	public void startConnection() {
		super.startConnection();
		runKeepAlive();
	}

	public void runKeepAlive() {
		if (writerRunning) {
			count = System.currentTimeMillis();
			keepAliveThread = new Thread(new Runnable() {

				@Override
				public void run() {
					while (writerRunning) {
						if (System.currentTimeMillis() > count + intervalKeepAlive) {
							messagesToSend.add("-t#k#a-");
							count = System.currentTimeMillis();
						} else {
							try {
								Thread.sleep(25);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
				}
			});
			keepAliveThread.start();
		}
	}

	@Override
	protected void handleInput(String input) {
		System.out.println("Received: "+input);
		count = System.currentTimeMillis(); // Resetting keep alive since a
											// message was received.
		if (input.startsWith("PING")) {
			addMessageToSend(input);
			return;
		}
		if (input.startsWith("TR")) {
			String[] values = input.split(" ");
			if(values.length  >= 2) {
				hostServer.newTradeMessage(input.substring(3));
				addMessageToSend("TRR");
			}
			return;
		}
		if(input.startsWith("UTG")) {
			String[] values = input.split(" ");
			if(values.length == 4) {
				String name = values[1];
				try {
					int warning = Integer.parseInt(values[2]);
					int layers = Integer.parseInt(values[3]);
					TradeGraphUtil.writeUserGraphFile(hostServer.getUserController().retrieveGraphForUser(name, layers, warning, DetailLevel.ALL), warning);
				} catch (Exception e) {
					e.printStackTrace();
					return;
				}
			}
		}
	}

	@Override
	public void exitConnection() {
		hostServer.requestCloseClient(connectionOwner);
	}

	public void setConnectionOwner(Client client) {
		connectionOwner = client;
	}



}