package org.ufla.dcc.equivalencyanalyse.exception;

public class GitException extends Exception {

  public static final String[] NOT_ERROR_MESSAGES = {"Cloning into '.*'\\.\\.\\.\n"};
  private static final long serialVersionUID = 1L;

  public GitException(String message) {
    super(message);
  }

}
