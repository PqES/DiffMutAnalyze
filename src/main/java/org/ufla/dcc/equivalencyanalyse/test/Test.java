package org.ufla.dcc.equivalencyanalyse.test;

import java.io.File;

public class Test {
	public static void main(String[] args) {
		File file = new File("");
		System.out.println(file.getAbsolutePath());
		int i = 2 << 20;
		System.out.println(i);
	}

}
