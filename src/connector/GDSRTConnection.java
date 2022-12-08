package connector;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

import connection.Connection;
import connection.Server;

public class GDSRTConnection extends Connection{

	private Server serverReference;
	
	public GDSRTConnection(Socket socket, BufferedReader reader, PrintWriter writer) {
		super(socket, reader, writer);
	}
	

	@Override
	protected void handleInput(String input) {
		if (input.equals("players deny")) {
			serverReference.setAcceptingPlayersState(false);
		} else if (input.equals("players accept")) {
			serverReference.setAcceptingPlayersState(true);
		} else if (input.startsWith("TR ")) {
			String[] values = input.split(" ");
			if(values.length == 2) {
				String msg = values[1];
				//TODO make decryption if chosen else not
			}
		}
	}
	
	protected boolean sendTrade(String trade) {
		writer.println(trade);
		return true;
	}
	
}
