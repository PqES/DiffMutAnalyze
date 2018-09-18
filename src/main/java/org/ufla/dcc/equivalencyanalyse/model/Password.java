package org.ufla.dcc.equivalencyanalyse.model;

public interface Password {

	boolean checkPassword(Password password);

	String getPassword();

	void setPassword(String password);

}
