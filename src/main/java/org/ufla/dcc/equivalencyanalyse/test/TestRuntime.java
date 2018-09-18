package org.ufla.dcc.equivalencyanalyse.test;

import org.ufla.dcc.equivalencyanalyse.exception.DecompressException;
import org.ufla.dcc.equivalencyanalyse.exception.GitException;
import org.ufla.dcc.equivalencyanalyse.model.entity.Project;
import org.ufla.dcc.equivalencyanalyse.util.CloseProcessUtil;
import org.ufla.dcc.equivalencyanalyse.util.VerifyExceptionOnErrorStream;

import java.io.File;

public class TestRuntime {

	private static final String DIRECTORY_AUXILIAR = "temp_directory_ax1296";

	public static void main(String[] args) throws Exception {
		Project project = new Project();
		project.setGitURL("https://github.com/github/hub");
		testGit(project);
		testCompress("NaiveOnlineJudge.zip");
	}

	private static void testCompress(String originalFilename) throws Exception {
		ProcessBuilder pb = new ProcessBuilder("unzip", "-qq", originalFilename, "-d", DIRECTORY_AUXILIAR);
		Process process = pb.start();
		new VerifyExceptionOnErrorStream(process, DecompressException.class);
		pb = new ProcessBuilder("rm", originalFilename);
		process = pb.start();
		process.waitFor();
		CloseProcessUtil.close(process);
	}

	private static void testGit(Project project) throws Exception {
		ProcessBuilder pb = new ProcessBuilder("mkdir", DIRECTORY_AUXILIAR);
		Process process = pb.start();
		process.waitFor();
		CloseProcessUtil.close(process);
		File directoryAuxiliar = new File(DIRECTORY_AUXILIAR);
		pb = new ProcessBuilder("git", "clone", project.getGitURL()).directory(directoryAuxiliar);
		process = pb.start();
		new VerifyExceptionOnErrorStream(process, GitException.class, GitException.NOT_ERROR_MESSAGES);
		CloseProcessUtil.close(process);
	}

}
