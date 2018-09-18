package org.ufla.dcc.equivalencyanalyse.model;

import java.io.Serializable;

public class Login extends AbstractPassword implements Serializable, Password {

	private static final long serialVersionUID = 1L;

	private String email;

	private String password;

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
