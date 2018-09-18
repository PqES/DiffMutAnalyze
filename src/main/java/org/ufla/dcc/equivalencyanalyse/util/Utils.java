package org.ufla.dcc.equivalencyanalyse.util;

import java.io.File;

public class Utils {

	private static final String JAVA_EXTENSION = ".java";
	private static final String MUTANT_DIRECTORY = "mutants" + File.separator;

	public static String filterMutantDirectorys(String filepath) {
		int mutantsIndex = filepath.indexOf(MUTANT_DIRECTORY);
		int endMutantsIndex = filepath.indexOf(File.separator, mutantsIndex + MUTANT_DIRECTORY.length());
		return filepath.substring(0, mutantsIndex) + filepath.substring(endMutantsIndex + 1);
	}

	public static String getMessagesRecursive(Throwable throwable) {
		StringBuilder sb = new StringBuilder(throwable.getMessage());
		throwable = throwable.getCause();
		while (throwable != null) {
			sb.append(" CAUSED BY ").append(throwable.getMessage());
			throwable = throwable.getCause();
		}
		return sb.toString();
	}

	public static boolean isJavaExtension(File file) {
		return file.getAbsolutePath().endsWith(JAVA_EXTENSION);
	}

	public static boolean isMutantFile(File file) {
		if (!isJavaExtension(file)) {
			return false;
		}
		return file.getAbsolutePath().contains(MUTANT_DIRECTORY);
	}

}
