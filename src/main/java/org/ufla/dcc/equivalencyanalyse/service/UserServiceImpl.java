package org.ufla.dcc.equivalencyanalyse.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ufla.dcc.equivalencyanalyse.model.Login;
import org.ufla.dcc.equivalencyanalyse.model.entity.User;
import org.ufla.dcc.equivalencyanalyse.repository.UserRepository;
import org.ufla.dcc.equivalencyanalyse.service.encrypt.EncryptService;

import javax.transaction.Transactional;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private EncryptService encryptService;

	@Override
	@Transactional
	public List<User> getUsers() {
		return userRepository.findAll();
	}

	@Override
	@Transactional
	public User register(User user) throws NoSuchAlgorithmException {
		encryptService.encrypt(user);
		return userRepository.save(user);
	}

	@Override
	@Transactional
	public User update(User user) {
		return userRepository.save(user);
	}

	@Override
	@Transactional
	public User validateUser(Login login) throws Exception {
		Optional<User> optionalUser = userRepository.findByEmail(login.getEmail());
		if (!optionalUser.isPresent()) {
			throw new Exception("Endereço de e-mail não está cadastrado no sistema!");
		}
		encryptService.encrypt(login);
		User user = optionalUser.get();
		if (user.checkPassword(login)) {
			return user;
		}
		throw new Exception("Senha inválida para esse endereço de e-mail.");
	}

}
