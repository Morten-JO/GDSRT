package util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class EncryptionHelper {

	public static String encryptMsgWithPublicKey(String msg, PublicKey publicKey) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
		Cipher encryptCipher = Cipher.getInstance("RSA");
		encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);
		byte[] msgToBytes = msg.getBytes(StandardCharsets.UTF_8);
		byte[] encryptedBytes = encryptCipher.doFinal(msgToBytes);
		return Base64.getEncoder().encodeToString(encryptedBytes);
	}
	
	public static String decryptMsgWithPrivateKey(String msg, PrivateKey privateKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		Cipher decryptCipher = Cipher.getInstance("RSA");
		decryptCipher.init(Cipher.DECRYPT_MODE, privateKey);
		byte[] msgToBytes = msg.getBytes(StandardCharsets.UTF_8);
		byte[] decryptedBytes = decryptCipher.doFinal(msgToBytes);
		return Base64.getEncoder().encodeToString(decryptedBytes);
	}
	
	public static PublicKey getKeyFileToPublicKey(File keyFile) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
		byte[] keyBytes = Files.readAllBytes(keyFile.toPath());
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		EncodedKeySpec pkSpec = new X509EncodedKeySpec(keyBytes);
		return keyFactory.generatePublic(pkSpec);
	}
	
}
