package org.ufla.dcc.equivalencyanalyse.service;

import org.ufla.dcc.equivalencyanalyse.model.Login;
import org.ufla.dcc.equivalencyanalyse.model.entity.User;

import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface UserService {

	List<User> getUsers();

	User register(User user) throws NoSuchAlgorithmException;

	User update(User user);

	User validateUser(Login login) throws Exception;

}