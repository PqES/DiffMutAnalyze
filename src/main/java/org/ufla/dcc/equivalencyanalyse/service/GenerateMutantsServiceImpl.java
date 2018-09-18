package org.ufla.dcc.equivalencyanalyse.service;

import org.springframework.stereotype.Service;
import org.ufla.dcc.equivalencyanalyse.model.entity.OriginalCode;
import org.ufla.dcc.equivalencyanalyse.model.entity.Project;
import org.ufla.dcc.equivalencyanalyse.service.storage.StorageProperties;
import org.ufla.dcc.equivalencyanalyse.util.CloseProcessUtil;
import org.ufla.dcc.equivalencyanalyse.util.VerifyExceptionOnErrorStream;
import javax.transaction.Transactional;
import java.io.File;

@Service
public class GenerateMutantsServiceImpl implements GenerateMutantsService {

  private static final String COMMAND_MAVEN = "mvn";
  private static final String MAVEN_PARAM_0 = "package";

  private static final String COMMAND_MAJOR = "../../major/bin/./javac";
  private static final String MAJOR_PARAM_0 = "-J-Dmajor.export.mutants=true";
  private static final String MAJOR_PARAM_1 = "-XMutator:ALL";
  private static final String MAJOR_PARAM_3 = "-cp";


  private File getTargetFinalJar(File projectFolder) throws Exception {
    File targetFolder = new File(projectFolder.getAbsolutePath() + File.separator + "target");
    if (!targetFolder.exists() || !targetFolder.isDirectory()) {
      throw new Exception("Error! Not find folder '" + targetFolder.getAbsolutePath() + "'.");
    }
    for (File file : targetFolder.listFiles()) {
      if (file.isDirectory()) {
        continue;
      }
      if (file.getAbsolutePath().endsWith(".jar")) {
        return file;
      }
    }
    throw new Exception("Error! Not find final jar in '" + targetFolder.getAbsolutePath() + "'.");
  }

  private String getRelativePath(File rootFolder, File file) {
    return file.getAbsolutePath().substring(rootFolder.getAbsolutePath().length() + 1);
  }

  @Override
  @Transactional
  public void generateMutants(Project project) throws Exception {
    String sourceDirectory = project.getDirectorySource();
    int indexSource = sourceDirectory.indexOf(File.separator + "src");
    String directoryProjectStr = StorageProperties.getInstance().getUploadDirectory()
        + File.separator + project.getHashkey();
    File projectDirectory =
        new File(directoryProjectStr + File.separator + sourceDirectory.substring(0, indexSource));
    ProcessBuilder pbMvn =
        new ProcessBuilder(COMMAND_MAVEN, MAVEN_PARAM_0).directory(projectDirectory);
    Process processMvn = pbMvn.start();
    processMvn.waitFor();
    new VerifyExceptionOnErrorStream(processMvn, Exception.class);
    CloseProcessUtil.close(processMvn);
    File directory = new File(directoryProjectStr);
    File finalJarFile = getTargetFinalJar(projectDirectory);
    String finalJarRelativePath = getRelativePath(directory, finalJarFile);
    for (OriginalCode originalCode : project.getOriginalCodes()) {
      ProcessBuilder pb = new ProcessBuilder(COMMAND_MAJOR, MAJOR_PARAM_0, MAJOR_PARAM_1,
          originalCode.getFilepath(), MAJOR_PARAM_3, finalJarRelativePath).directory(directory);
      Process process = pb.start();
      process.waitFor();
      // new VerifyExceptionOnErrorStream(process, Exception.class);
      CloseProcessUtil.close(process);
    }
  }

}
