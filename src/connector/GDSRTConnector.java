package connector;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import util.EncryptionHelper;

public class GDSRTConnector {
	
	private final boolean sendWithEncryption;
	private PublicKey serversPublicKey;
	private String ip;
	private int port;
	
	private GDSRTConnection gdsrtConnection;

	public static final int INCOMING_SERVER_PORT = 1239;
	
	
	public GDSRTConnector(String ip, int port) throws IOException, ClassNotFoundException, SQLException {
		this.ip = ip;
		this.port = port;
		this.sendWithEncryption = false;
	}
	
	public GDSRTConnector(String ip, int port, File publicKeyFile) throws IOException, ClassNotFoundException, SQLException {
		this.ip = ip;
		this.port = port;
		this.sendWithEncryption = true;
		try {
			this.serversPublicKey = EncryptionHelper.getKeyFileToPublicKey(publicKeyFile);
		} catch (NoSuchAlgorithmException | InvalidKeySpecException | IOException e) {
			System.err.println("Failed to load the public key file, are you sure you specified the right location?");
			e.printStackTrace();
			System.exit(0);
		}
	}

	public boolean run() throws UnknownHostException, IOException{
		Socket gdscrtSocket = new Socket(ip, port);
		BufferedReader reader = new BufferedReader(new InputStreamReader(gdscrtSocket.getInputStream()));
		PrintWriter writer = new PrintWriter(gdscrtSocket.getOutputStream(), true);	
		gdsrtConnection = new GDSRTConnection(gdscrtSocket, reader, writer);
		return true;
	}

	public void addToDebug(String string) {

	}
	
	public boolean addTrade(Map<String, String> map) {
		StringBuilder mapAsString = new StringBuilder("{");
	    for (String key : map.keySet()) {
	        mapAsString.append(key + "=" + map.get(key) + ", ");
	    }
	    mapAsString.delete(mapAsString.length()-2, mapAsString.length()).append("}");
	    return addMessageForTransportation(mapAsString.toString());
	}
	
	public boolean addTrade(String string) {
		return addMessageForTransportation(string);
	}
	
	private boolean addMessageForTransportation(String msg) {
		if(sendWithEncryption) {
			try {
				String encryptedMsg = EncryptionHelper.encryptMsgWithPublicKey(msg, serversPublicKey);
				gdsrtConnection.addMessageToSend(encryptedMsg);
				return true;
			} catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException | NoSuchAlgorithmException
					| NoSuchPaddingException e) {
				e.printStackTrace();
				return false;
			}
			
		} else {
			return gdsrtConnection.addMessageToSend(msg);
		}
	}
	
	public GDSRTConnection getConnection() {
		return gdsrtConnection;
	}
}
