package org.ufla.dcc.equivalencyanalyse.util;

import java.io.IOException;

public class CloseProcessUtil {

	public static void close(Process process) throws IOException {
		if (process.getErrorStream() != null) {
			process.getErrorStream().close();
		}
		process.getInputStream().close();
		process.getOutputStream().close();
	}

}
