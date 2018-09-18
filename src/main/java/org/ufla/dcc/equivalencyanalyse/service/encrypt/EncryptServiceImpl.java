package org.ufla.dcc.equivalencyanalyse.service.encrypt;

import org.springframework.stereotype.Service;
import org.ufla.dcc.equivalencyanalyse.model.Password;
import org.ufla.dcc.equivalencyanalyse.model.entity.OriginalCode;
import org.ufla.dcc.equivalencyanalyse.model.entity.Project;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class EncryptServiceImpl implements EncryptService {

	private final static String ENCRYPT_ALGORITHM = "SHA-256";

	@Override
	public String encrypt(File file) throws NoSuchAlgorithmException, IOException {
		MessageDigest messageDigest = MessageDigest.getInstance(ENCRYPT_ALGORITHM);
		messageDigest.reset();
		InputStream inputStream = Files.newInputStream(file.toPath());
		DigestInputStream digestInputStream = new DigestInputStream(inputStream, messageDigest);
		while (true) {
			if (digestInputStream.read() == -1)
				break;
		}
		return toHexString(messageDigest.digest());

	}

	@Override
	public void encrypt(Password password) throws NoSuchAlgorithmException {
		String pass = password.getPassword();
		String encyptPass = encrypt(pass);
		password.setPassword(encyptPass);
	}

	@Override
	public void encrypt(Project project) throws Exception {
		if (project == null || project.getOriginalCodes() == null) {
			throw new Exception("Não é possível gerar a hash de um projeto NULL");
		}
		MessageDigest messageDigest = MessageDigest.getInstance(ENCRYPT_ALGORITHM);
		messageDigest.reset();
		for (OriginalCode originalCode : project.getOriginalCodes()) {
			messageDigest.update(originalCode.getHashkey().getBytes());
		}
		project.setHashkey(toHexString(messageDigest.digest()));
	}

	@Override
	public String encrypt(String word) throws NoSuchAlgorithmException {
		MessageDigest messageDigest = MessageDigest.getInstance(ENCRYPT_ALGORITHM);
		messageDigest.reset();
		byte wordBytes[] = word.getBytes();
		byte[] digested = messageDigest.digest(wordBytes);
		return toHexString(digested);
	}

	private String toHexString(byte[] bytes) {
		StringBuilder hexString = new StringBuilder();
		int i = 0;
		while (i < bytes.length) {
			byte aByte = bytes[i];
			String hex = Integer.toHexString(0xFF & aByte);
			if (hex.length() == 1) {
				hexString.append('0');
			}
			hexString.append(hex);
			i++;
		}
		return hexString.toString();
	}

}
