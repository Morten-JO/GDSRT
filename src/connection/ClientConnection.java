package connection;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;


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
		count = System.currentTimeMillis(); // Resetting keep alive since a
											// message was received.
		if (input.startsWith("PING")) {
			addMessageToSend(input);
			return;
		}
		if (input.startsWith("LR")) {
			String[] values = input.split(" ");
			if(values.length  >= 2) {
				hostServer.newTradeMessage(values[1]);
			}
			return;
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