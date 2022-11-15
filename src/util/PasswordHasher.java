package util;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class PasswordHasher {
	
	public static String getSHA512SecurePassword(String passwordToHash, String salt){
		String generatedPassword = null;
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-512");
			md.update(salt.getBytes(StandardCharsets.UTF_8));
			byte[] bytes = md.digest(passwordToHash.getBytes(StandardCharsets.UTF_8));
			StringBuilder sb = new StringBuilder();
			for(int i = 0; i < bytes.length; i++){
				sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
			}
			generatedPassword = sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		if(generatedPassword != null){
			if(generatedPassword.length() > 250){
				generatedPassword = generatedPassword.substring(0, 249);
			}
		}
		return generatedPassword;
	}
	
	public static String byte2string(byte[] bytes){
		BigInteger number = new BigInteger(1, bytes);
		String stringText = number.toString(16);
			
		while(stringText.length() < 32) {
			stringText = "0" + stringText;
		}
		if(stringText.length() > 70){
			stringText = stringText.substring(0, 69);
		}
		return stringText;
	}
	
	public static String getRandomSalt(){
		SecureRandom random = new SecureRandom();
		byte[] salt = new byte[16];
		random.nextBytes(salt);
		salt = byte2string(salt).getBytes();
		return byte2string(salt);
	}
}