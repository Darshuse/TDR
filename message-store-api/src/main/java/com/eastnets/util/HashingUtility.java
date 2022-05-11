package com.eastnets.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class HashingUtility {

	private static final Logger LOGGER = LogManager.getLogger(HashingUtility.class);

	public static String getHashedPassword(String password, byte[] salt) {
		String generatedPassword = null;
		try {
			// Encrypt passwords using one-way techniques, this is, digests.
			// for creating digests. This class allows to specify the digest algorithm we
			// wish to use.
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(salt);
			// getBytes for obtaining a byte sequence from the input String, specifying a
			// fixed encoding ("UTF-8").
			// Generate the salted hash
			byte[] hashedPassword = md.digest(password.getBytes(StandardCharsets.UTF_8));
			generatedPassword = Base64.getEncoder().encodeToString(hashedPassword);
		} catch (NoSuchAlgorithmException e) {
			LOGGER.error(e.getMessage());
		}
		return generatedPassword;
	}

	public static byte[] getSalt() throws NoSuchAlgorithmException {
		// generating random salt in a secure manner, using algorithms like SHA1PRNG.
		SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
		byte[] salt = new byte[16];
		sr.nextBytes(salt);
		return salt;
	}

	public static void main(String[] args) throws NoSuchAlgorithmException {
		String passwordToHash = "edis";

		byte[] salt = getSalt();
		// LOGGER.debug("salt: " + Base64.getEncoder().encodeToString(salt));
		String securePassword = getHashedPassword(passwordToHash, salt);
		// LOGGER.debug("HashedPassword: " + securePassword);

	}

}
