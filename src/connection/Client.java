package connection;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
	// Tcp section
	protected ClientConnection connection;

	public Client(Server hostServer, Socket socket, PrintWriter writer,
			BufferedReader reader) {
		connection = new ClientConnection(this, hostServer, socket, reader, writer);
	}

	public Client() {

	}

	public ClientConnection getConnection() {
		return connection;
	}

	public boolean addMessageToSend(String message) {
		if (connection != null) {
			connection.addMessageToSend(message);
			return true;
		}
		return false;
	}

	public void removeConnection() {
		connection = null;
	}

	public void setConnection(ClientConnection connection) {
		this.connection = connection;
	}
}