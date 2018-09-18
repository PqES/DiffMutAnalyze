package org.ufla.dcc.equivalencyanalyse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.ufla.dcc.equivalencyanalyse.model.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByEmail(String email);

}
