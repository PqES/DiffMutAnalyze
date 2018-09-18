package org.ufla.dcc.equivalencyanalyse.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ufla.dcc.equivalencyanalyse.model.entity.MutantCode;
import org.ufla.dcc.equivalencyanalyse.model.entity.OriginalCode;
import org.ufla.dcc.equivalencyanalyse.model.entity.Project;
import org.ufla.dcc.equivalencyanalyse.repository.MutantCodeRepository;
import org.ufla.dcc.equivalencyanalyse.repository.ProjectRepository;
import org.ufla.dcc.equivalencyanalyse.service.storage.StorageProperties;
import org.ufla.dcc.equivalencyanalyse.util.CloseProcessUtil;

@Service
public class MutantsTestServiceMavenImpl implements MutantsTestService {

  private static final String COMMAND_MAVEN = "mvn";
  private static final String MAVEN_PARAM_0 = "test";

  @Autowired
  private ProjectRepository projectRepository;

  @Autowired
  private MutantCodeRepository mutantCodeRepository;

  @Override
  public void testAllMutants(Project project) throws Exception {
    project = projectRepository.getOne(project.getId());
    String projectPath = StorageProperties.getInstance().getUploadDirectory() + File.separator
        + project.getHashkey() + File.separator;
    String sourceDirectory = project.getDirectorySource();
    int indexSource = sourceDirectory.indexOf(File.separator + "src");
    File projectMainFolder = new File(projectPath + sourceDirectory.substring(0, indexSource));
    for (OriginalCode originalCode : project.getOriginalCodes()) {
      testAllMutants(originalCode, projectPath, projectMainFolder);
    }
  }

  private void backupOriginalCode(OriginalCode originalCode, String projectPath) throws Exception {
    File originalCodeFile = new File(projectPath + originalCode.getFilepath());
    if (!originalCodeFile.renameTo(new File(projectPath + originalCode.getFilepath() + ".bk"))) {
      throw new Exception("Erro! Não foi possível realizar o backup do código-fonte '"
          + originalCode.getFilepath() + "'!");
    }
  }

  private void restoreOriginalCode(OriginalCode originalCode, String projectPath) throws Exception {
    File originalCodeBkFile = new File(projectPath + originalCode.getFilepath() + ".bk");
    if (!originalCodeBkFile.renameTo(new File(projectPath + originalCode.getFilepath()))) {
      throw new Exception("Erro! Não foi possível restaurar o backup do código-fonte '"
          + originalCode.getFilepath() + "'!");
    }
  }

  private void testAllMutants(OriginalCode originalCode, String projectPath, File projectMainFolder)
      throws Exception {
    if (originalCode.getMutantCodes() != null) {
      backupOriginalCode(originalCode, projectPath);
      for (MutantCode mutantCode : originalCode.getMutantCodes()) {
        testMutant(mutantCode, projectPath, projectMainFolder);
      }
      restoreOriginalCode(originalCode, projectPath);
    }
  }

  private void moveTestMutant(MutantCode mutantCode, String projectPath) throws Exception {
    File mutantCodeFile = new File(projectPath + mutantCode.getFilepath());
    if (!mutantCodeFile
        .renameTo(new File(projectPath + mutantCode.getOriginalCode().getFilepath()))) {
      throw new Exception("Erro! Não foi possível mover o mutante '" + mutantCode.getFilepath()
          + "' para executar os testes!");
    }
  }

  private void moveBackTestMutant(MutantCode mutantCode, String projectPath) throws Exception {
    File mutantCodeFile = new File(projectPath + mutantCode.getOriginalCode().getFilepath());
    if (!mutantCodeFile.renameTo(new File(projectPath + mutantCode.getFilepath()))) {
      throw new Exception("Erro! Não foi possível voltar o mutante '" + mutantCode.getFilepath()
          + "' após executar os testes!");
    }
  }

  private String extractMessage(InputStream inputStream) throws IOException {
    byte[] bytes = new byte[inputStream.available()];
    inputStream.read(bytes, 0, bytes.length);
    return new String(bytes);
  }

  private static final String TESTS_START = " T E S T S";
  private static final String TESTS_RESULTS_START = "Results :";
  private static final String TESTS_FAILURE_START = ", Failures: ";
  private static final String TESTS_FAILURE_END = ",";

  private void verifyIndex(String message, int index) throws Exception {
    if (index == -1) {
      throw new Exception("Erro no relatório do Maven:\n" + message);
    }
  }

  private boolean someTestHaveFailed(String message) throws Exception {
    int startFailureIndex = message.indexOf(TESTS_START);
    verifyIndex(message, startFailureIndex);
    startFailureIndex = message.indexOf(TESTS_RESULTS_START, startFailureIndex);
    verifyIndex(message, startFailureIndex);
    startFailureIndex = message.indexOf(TESTS_FAILURE_START, startFailureIndex);
    verifyIndex(message, startFailureIndex);
    startFailureIndex += TESTS_FAILURE_START.length();
    int endFailureIndex = message.indexOf(TESTS_FAILURE_END, startFailureIndex);
    verifyIndex(message, endFailureIndex);
//    System.out.println("TEST FAILED: ");
//    System.out.println(message.substring(message.indexOf(TESTS_RESULTS_START), endFailureIndex));
    try {
      if (Integer.parseInt(message.substring(startFailureIndex, endFailureIndex)) == 0) {
        return false;
      }
      return true;
    } catch (Exception e) {
      throw new Exception("Erro no relatório do Maven(" + startFailureIndex + "," + endFailureIndex
          + "): " + message.substring(startFailureIndex, endFailureIndex) + "\n" + message);
    }
  }

  private void testMutant(MutantCode mutantCode, String projectPath, File projectMainFolder)
      throws Exception {
    moveTestMutant(mutantCode, projectPath);
    ProcessBuilder pb =
        new ProcessBuilder(COMMAND_MAVEN, MAVEN_PARAM_0).directory(projectMainFolder);
    Process process = pb.start();
    process.waitFor();
    String message = null;
    try {
      message = extractMessage(process.getInputStream());
    } catch (IOException e) {
      throw new Exception(
          "Erro! Não foi possível ler a saída da execução dos testes de unidade no mutante '"
              + mutantCode.getFilepath() + "'!");
    }
    CloseProcessUtil.close(process);
    moveBackTestMutant(mutantCode, projectPath);
    if (someTestHaveFailed(message)) {
      mutantCode.setDead(true);
      mutantCodeRepository.save(mutantCode);
    }
  }

}
