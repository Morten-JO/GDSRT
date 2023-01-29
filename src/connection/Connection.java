package connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

public class Connection {

	protected Socket socket;
	protected BufferedReader reader;
	protected PrintWriter writer;

	protected Thread readerThread;
	protected Thread writerThread;

	protected boolean readerRunning = false;
	protected boolean writerRunning = false;

	protected ArrayList<String> messagesToSend;

	private int invalidDataPacketsInRow = 0;
	private final int INVALID_DATA_PACKET_REQIUREMENT = 50;

	public Connection(Socket socket, BufferedReader reader, PrintWriter writer) {
		this.socket = socket;
		this.reader = reader;
		this.writer = writer;
		messagesToSend = new ArrayList<>();
	}

	public void startConnection() {
		runReader();
		runWriter();
	}

	public void runReader() {
		if (!readerRunning) {
			readerRunning = true;
			readerThread = new Thread(new Runnable() {

				@Override
				public void run() {
					while (readerRunning) {
						String input = "";
						try {
							input = reader.readLine();
							if (input != null) {
								handleInput(input);
								invalidDataPacketsInRow = 0;
							} else {
								invalidDataPacketsInRow++;
								if (invalidDataPacketsInRow > INVALID_DATA_PACKET_REQIUREMENT) {
									closeConnection();
								}
							}

						} catch (SocketException e) {
							invalidDataPacketsInRow++;
							if (invalidDataPacketsInRow > INVALID_DATA_PACKET_REQIUREMENT) {
								closeConnection();
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					exitConnection();
				}

			});
			readerThread.start();
		}
	}

	public void runWriter() {
		if (!writerRunning) {
			writerRunning = true;
			writerThread = new Thread(new Runnable() {

				@Override
				public void run() {
					while (writerRunning) {
						if (messagesToSend.size() > 0) {
							writer.println(messagesToSend.get(0));
							messagesToSend.remove(0);
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
			writerThread.start();
		}
	}

	protected void handleInput(String input) {
	}

	public boolean addMessageToSend(String message) {
		synchronized (messagesToSend) {
			messagesToSend.add(message);
			return true;
		}
	}

	private void closeConnection() {
		readerRunning = false;
		writerRunning = false;
	}

	public String getIp() {
		return socket.getInetAddress().getHostAddress();
	}

	public void exitConnection() {
		try {
			socket.close();
		} catch (IOException e) {
		}
	}

	public boolean isConnectionActive() {
		if (socket != null) {
			if (socket.isConnected()) {
				return true;
			}
		}
		return false;
	}
}