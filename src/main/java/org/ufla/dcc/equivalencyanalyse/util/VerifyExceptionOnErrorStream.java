package org.ufla.dcc.equivalencyanalyse.util;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;

public class VerifyExceptionOnErrorStream extends Thread {

	private Process process;
	private String errorMessage;
	private Class<? extends Exception> exceptionClass;
	private String[] notErrorMessages;

	public VerifyExceptionOnErrorStream(Process process, Class<? extends Exception> exceptionClass) throws Exception {
		this(process, exceptionClass, null);
	}

	public VerifyExceptionOnErrorStream(Process process, Class<? extends Exception> exceptionClass,
			String[] notErrorMessages) throws Exception {
		this.process = process;
		this.exceptionClass = exceptionClass;
		this.errorMessage = null;
		this.notErrorMessages = notErrorMessages;
		this.verifyException();
	}

	private boolean hasError() {
		if (this.errorMessage == null) {
			return false;
		}
		if (this.notErrorMessages == null) {
			return true;
		}
		for (String notErrorMessage : notErrorMessages) {
			if (this.errorMessage.matches(notErrorMessage)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public void run() {
		try {
			runIntern();
		} catch (Exception e) {
			errorMessage = "Error on ExtractErrorMessageProcess!\n" + e.getMessage();
			e.printStackTrace();
		}
	}

	private void runIntern() throws InterruptedException, IOException {
		InputStream errorStream = process.getErrorStream();
		int error = 0;
		int newError = 0;
		boolean alive = true;
		do {
			error = newError;
			Thread.sleep(500);
			alive = process.isAlive();
			newError = errorStream.available();
		} while (alive && (error == 0 || error != newError));
		if (error != 0 || error != newError) {
			byte[] messageByte = new byte[errorStream.available()];
			errorStream.read(messageByte);
			this.errorMessage = new String(messageByte);
			if (this.hasError()) {
				process.destroy();
			} else {
				runIntern();
			}
		}
	}

	private void verifyException() throws Exception {
		this.start();
		this.join();
		process.waitFor();
		CloseProcessUtil.close(process);
		if (hasError()) {
			Constructor<? extends Exception> exceptionConstruct = exceptionClass.getConstructor(String.class);
			throw exceptionConstruct.newInstance(this.errorMessage);
		}
	}

}
