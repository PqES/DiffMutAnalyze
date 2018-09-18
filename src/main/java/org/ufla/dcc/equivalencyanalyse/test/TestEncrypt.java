package org.ufla.dcc.equivalencyanalyse.test;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import org.ufla.dcc.equivalencyanalyse.service.encrypt.EncryptService;
import org.ufla.dcc.equivalencyanalyse.service.encrypt.EncryptServiceImpl;

public class TestEncrypt {
	public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
		EncryptService encryptService = new EncryptServiceImpl();
		System.out.println(encryptService.encrypt("value"));
		File file1 = new File("src/org/ufla/equivalencyanalyse/controller/ProjectController.java");
		File file2 = new File("src/org/ufla/equivalencyanalyse/controller/UserController.java");
		System.out.println(file1.getAbsolutePath() + ":");
		System.out.println(encryptService.encrypt(file1));
		System.out.println(file2.getAbsolutePath() + ":");
		System.out.println(encryptService.encrypt(file2));

	}

}
