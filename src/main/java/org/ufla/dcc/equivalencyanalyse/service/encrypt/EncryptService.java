package org.ufla.dcc.equivalencyanalyse.service.encrypt;

import org.ufla.dcc.equivalencyanalyse.model.Password;
import org.ufla.dcc.equivalencyanalyse.model.entity.Project;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public interface EncryptService {

	String encrypt(File file) throws NoSuchAlgorithmException, IOException;

	void encrypt(Password password) throws NoSuchAlgorithmException;

	void encrypt(Project project) throws Exception;

	String encrypt(String word) throws NoSuchAlgorithmException;

}
