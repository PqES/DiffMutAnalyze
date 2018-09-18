package org.ufla.dcc.equivalencyanalyse.model;

public abstract class AbstractPassword implements Password {

	public boolean checkPassword(Password password) {
		if (password == null || this.getPassword() == null) {
			return false;
		}
		return this.getPassword().equals(password.getPassword());
	}

}
