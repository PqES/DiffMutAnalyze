package org.ufla.dcc.equivalencyanalyse.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.ufla.dcc.equivalencyanalyse.dto.projectinfo.MutantCodeInfo;
import org.ufla.dcc.equivalencyanalyse.dto.projectinfo.MutantReportInfo;
import org.ufla.dcc.equivalencyanalyse.dto.projectinfo.OriginalCodeInfo;
import org.ufla.dcc.equivalencyanalyse.dto.projectinfo.ProjectInfo;
import org.ufla.dcc.equivalencyanalyse.exception.GitException;
import org.ufla.dcc.equivalencyanalyse.exception.MutantHasNoReference;
import org.ufla.dcc.equivalencyanalyse.model.entity.*;
import org.ufla.dcc.equivalencyanalyse.repository.MutantCodeRepository;
import org.ufla.dcc.equivalencyanalyse.repository.MutantReportRepository;
import org.ufla.dcc.equivalencyanalyse.repository.OriginalCodeRepository;
import org.ufla.dcc.equivalencyanalyse.repository.ProjectAdminRepository;
import org.ufla.dcc.equivalencyanalyse.repository.ProjectRepository;
import org.ufla.dcc.equivalencyanalyse.service.encrypt.EncryptService;
import org.ufla.dcc.equivalencyanalyse.service.mail.MailerService;
import org.ufla.dcc.equivalencyanalyse.service.storage.StorageProperties;
import org.ufla.dcc.equivalencyanalyse.service.storage.StorageService;
import org.ufla.dcc.equivalencyanalyse.util.CloseProcessUtil;
import org.ufla.dcc.equivalencyanalyse.util.Utils;
import org.ufla.dcc.equivalencyanalyse.util.VerifyExceptionOnErrorStream;
import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Service
public class ProjectServiceImpl implements ProjectService {

  private static final String TEMP_DIRECTORY = "temp_directory_";
  private static final String[] SOURCE_DIRECTORYS =
      {"src" + File.separator + "main" + File.separator + "java" + File.separator,
          "src" + File.separator};

  public static String getCompleteTempDirectory(int idTempDirectory) {
    return StorageProperties.getInstance().getUploadDirectory() + File.separator
        + getSimpleNameTempDirectory(idTempDirectory);
  }

  private static String getSimpleNameTempDirectory(int idTempDirectory) {
    return TEMP_DIRECTORY + idTempDirectory;
  }

  @Autowired
  private ProjectRepository projectRepository;

  @Autowired
  private MutantCodeRepository mutantCodeRepository;


  @Autowired
  private MutantReportRepository mutantReportRepository;

  @Autowired
  private OriginalCodeRepository originalCodeRepository;

  @Autowired
  private MailerService mailerService;

  @Autowired
  private ProjectAdminRepository projectAdminRepository;

  @Autowired
  private StorageService storageService;

  @Autowired
  private EncryptService encryptService;

  @Autowired
  private GenerateMutantsService generateMutantsService;

  @Autowired
  private MutantsTestService mutantsTestService;


  private void associateMutants(List<OriginalCode> originalCodes, List<MutantCode> mutantCodes,
      String sourceDirectory) throws MutantHasNoReference {
    Map<String, OriginalCode> filepathToCode = mapFilepathToCode(originalCodes);
    for (MutantCode mutantCode : mutantCodes) {
      String filepathOriginalCode =
          sourceDirectory + Utils.filterMutantDirectorys(mutantCode.getFilepath());
      OriginalCode originalCode = filepathToCode.get(filepathOriginalCode);
      if (originalCode == null) {
        throw new MutantHasNoReference(
            String.format("Erro! O mutante %s não possui seu código referência que deveria ser %s.",
                mutantCode.getFilepath(), filepathOriginalCode));
      }
      originalCode.addMutantCode(mutantCode);
    }
  }

  private void changeDirectory(Project project, int idTempDirectory) throws Exception {
    File rootDirectory = new File(getCompleteTempDirectory(idTempDirectory));
    String newDirectoryName = StorageProperties.getInstance().getUploadDirectory() + File.separator
        + project.getHashkey();
    if (!rootDirectory.renameTo(new File(newDirectoryName))) {
      throw new Exception("Erro! Não foi possível definir o diretório desse projeto!");
    }
  }

  private void extractCode(File file, String tempDirectory, AbstractCode abstractCode)
      throws NoSuchAlgorithmException, IOException {
    String filepath = file.getAbsolutePath();
    int indexBegin = filepath.lastIndexOf(File.separator);
    abstractCode.setFilename(filepath.substring(indexBegin + 1));
    indexBegin = filepath.indexOf(tempDirectory);
    abstractCode.setFilepath(filepath.substring(indexBegin + 1 + tempDirectory.length()));
    abstractCode.setHashkey(encryptService.encrypt(file));
  }

  private void extractCodes(File rootDirectory, List<OriginalCode> originalCodes,
      List<MutantCode> mutantCodes, String tempDirectory)
      throws NoSuchAlgorithmException, IOException {
    for (File file : Objects.requireNonNull(rootDirectory.listFiles())) {
      if (file.isDirectory()) {
        extractCodes(file, originalCodes, mutantCodes, tempDirectory);
      } else if (Utils.isMutantFile(file)) {
        mutantCodes.add(extractMutantCode(file, tempDirectory));
      } else if (Utils.isJavaExtension(file) && isOnSourceDirectory(file)) {
        originalCodes.add(extractOriginalCode(file, tempDirectory));
      }
    }
  }

  private MutantCode extractMutantCode(File file, String tempDirectory)
      throws NoSuchAlgorithmException, IOException {
    MutantCode mutantCode = new MutantCode();
    extractCode(file, tempDirectory, mutantCode);
    mutantCode.setDead(false);
    return mutantCode;
  }

  private void extractMutantCodes(Project project)
      throws NoSuchAlgorithmException, IOException, MutantHasNoReference {
    Set<String> mutantsFilepath = setMutantsFilepath(project);
    List<MutantCode> mutantCodes = new ArrayList<>();
    String directoryName = StorageProperties.getInstance().getUploadDirectory() + File.separator
        + project.getHashkey();
    File directory = new File(directoryName);
    extractMutantCodesIntern(directory, mutantCodes, mutantsFilepath, directoryName);
    associateMutants(project.getOriginalCodes(), mutantCodes, project.getDirectorySource());
  }

  private void extractMutantCodesIntern(File rootDirectory, List<MutantCode> mutantCodes,
      Set<String> mutantsFilepath, String directoryStr)
      throws NoSuchAlgorithmException, IOException {
    for (File file : Objects.requireNonNull(rootDirectory.listFiles())) {
      if (file.isDirectory()) {
        extractMutantCodesIntern(file, mutantCodes, mutantsFilepath, directoryStr);
      } else if (Utils.isMutantFile(file)) {
        MutantCode mutantCode = extractMutantCode(file, directoryStr);
        if (!mutantsFilepath.contains(mutantCode.getFilepath())) {
          mutantCodes.add(mutantCode);
        }
      }
    }
  }

  private OriginalCode extractOriginalCode(File file, String tempDirectory)
      throws NoSuchAlgorithmException, IOException {
    OriginalCode originalCode = new OriginalCode();
    extractCode(file, tempDirectory, originalCode);
    return originalCode;
  }

  private void extractProjectInfo(Project project, int idTempDirectory) throws Exception {
    File tempDirectory = new File(getCompleteTempDirectory(idTempDirectory));
    List<OriginalCode> originalCodes = new ArrayList<>();
    List<MutantCode> mutantCodes = new ArrayList<>();
    extractCodes(tempDirectory, originalCodes, mutantCodes,
        getSimpleNameTempDirectory(idTempDirectory));
    String sourceDirectory = originalCodes.get(0).getFilepath();
    int lenght = 0;
    int index = 0;
    for (String srcDir : SOURCE_DIRECTORYS) {
      if ((index = sourceDirectory.indexOf(srcDir)) != -1) {
        lenght = srcDir.length();
        break;
      }
    }
    sourceDirectory = sourceDirectory.substring(0, index + lenght);
    project.setDirectorySource(sourceDirectory);
    associateMutants(originalCodes, mutantCodes, sourceDirectory);
    project.setOriginalCodes(originalCodes);
    for (OriginalCode originalCode : originalCodes) {
      originalCode.setProject(project);
    }
    encryptService.encrypt(project);
  }

  private boolean isOnSourceDirectory(File file) {
    String filepath = file.getAbsolutePath();
    if (filepath.contains("src" + File.separator + "test")) {
      return false;
    }
    for (String sourceDirectory : SOURCE_DIRECTORYS) {
      if (filepath.contains(sourceDirectory)) {
        return true;
      }
    }
    return false;
  }

  private Map<String, OriginalCode> mapFilepathToCode(List<OriginalCode> originalCodes) {
    Map<String, OriginalCode> filepathToCode = new HashMap<>();
    for (OriginalCode originalCode : originalCodes) {
      filepathToCode.put(originalCode.getFilepath(), originalCode);
    }
    return filepathToCode;
  }

  @Override
  @Transactional
  @Async
  public void registerProject(Project project, User user, int idTempDirectory,
      boolean registerGit) {
    try {
      if (registerGit) {
        registerProjectGit(project, idTempDirectory);
      }
      registerProjectIntern(project, user, idTempDirectory);
    } catch (Exception e) {
      e.printStackTrace();
      try {
        mailerService.sendEmailRegisterProjectError(project, e.getMessage());
      } catch (MessagingException | IOException e1) {
        e1.printStackTrace();
      }
    }
  }

  @Async
  private void registerProjectIntern(Project project, User user, int idTempDirectory)
      throws Exception {
    project.setOwner(user);
    project.setProjectAdmins(new ArrayList<>());
    project.setWithCode(true);
    extractProjectInfo(project, idTempDirectory);
    changeDirectory(project, idTempDirectory);
    generateMutantsService.generateMutants(project);
    extractMutantCodes(project);
    projectRepository.save(project);
    ProjectAdmin projectAdmin = new ProjectAdmin();
    projectAdmin.setAdmin(user);
    projectAdmin.setProject(project);
    projectAdminRepository.save(projectAdmin);
    for (OriginalCode originalCode : project.getOriginalCodes()) {
      originalCodeRepository.save(originalCode);
      if (originalCode.getMutantCodes() == null) {
        continue;
      }
      for (MutantCode mutantCode : originalCode.getMutantCodes()) {
        mutantCodeRepository.save(mutantCode);
      }
    }
    mutantsTestService.testAllMutants(project);
    mailerService.sendEmailRegisterProject(project);
  }

  public void registerProjectCompress(MultipartFile projectFile, int idTempDirectory) {
    storageService.store(projectFile, getSimpleNameTempDirectory(idTempDirectory));
  }

  private void registerProjectGit(Project project, int idTempDirectory) throws Exception {
    String completeTempDirectory = getCompleteTempDirectory(idTempDirectory);
    ProcessBuilder pb = new ProcessBuilder("mkdir", completeTempDirectory);
    Process process = pb.start();
    process.waitFor();
    CloseProcessUtil.close(process);
    File directoryAuxiliar = new File(completeTempDirectory);
    pb = new ProcessBuilder("git", "clone", project.getGitURL()).directory(directoryAuxiliar);
    process = pb.start();
    new VerifyExceptionOnErrorStream(process, GitException.class, GitException.NOT_ERROR_MESSAGES);
  }

  private Set<String> setMutantsFilepath(Project project) {
    Set<String> mutantsFilepath = new HashSet<>();
    for (OriginalCode originalCode : project.getOriginalCodes()) {
      if (originalCode.getMutantCodes() == null) {
        continue;
      }
      for (MutantCode mutantCode : originalCode.getMutantCodes()) {
        mutantsFilepath.add(mutantCode.getFilepath());
      }
    }
    return mutantsFilepath;
  }

  @Override
  @Transactional
  public ProjectInfo getProjectInfo(User user, String projectHash) {
    Project project = projectRepository.findByHashkey(projectHash);
    ProjectInfo projectInfo = new ProjectInfo(project);
    for (OriginalCodeInfo originalCodeInfo : projectInfo.getOriginalCodes()) {
      for (MutantCodeInfo mutantCodeInfo : originalCodeInfo.getMutantCodes()) {
        Optional<MutantReport> opMutantReport =
            mutantReportRepository.findReportByUserAndMutant(user.getId(), mutantCodeInfo.getId());
        if (opMutantReport.isPresent()) {
          mutantCodeInfo.setReport(new MutantReportInfo(opMutantReport.get()));
        }
      }
    }
    return projectInfo;
  }

  @Override
  @Transactional
  public List<Project> myProjects(User user) {
    List<Project> myProjects = projectRepository.findMyProjects(user.getId());
    List<Project> managedProjects = projectRepository.findMyManagedProjects(user.getId());
    List<Project> allMyProjects = new ArrayList<>(myProjects.size() + managedProjects.size());
    int myProjectsIndex = 0;
    int managedProjectsIndex = 0;
    while (myProjectsIndex < myProjects.size() && managedProjectsIndex < managedProjects.size()) {
      Project myProject = myProjects.get(myProjectsIndex);
      Project managedProject = managedProjects.get(managedProjectsIndex);
      if (myProject.getId() == managedProject.getId()) {
        allMyProjects.add(myProject);
        myProjectsIndex++;
        managedProjectsIndex++;
      } else if (myProject.getId() < managedProject.getId()) {
        allMyProjects.add(myProject);
        myProjectsIndex++;
      } else {
        allMyProjects.add(managedProject);
        managedProjectsIndex++;
      }
    }
    while (myProjectsIndex < myProjects.size()) {
      Project myProject = myProjects.get(myProjectsIndex);
      allMyProjects.add(myProject);
      myProjectsIndex++;
    }
    while (managedProjectsIndex < managedProjects.size()) {
      Project managedProject = managedProjects.get(managedProjectsIndex);
      allMyProjects.add(managedProject);
      managedProjectsIndex++;
    }
    return allMyProjects;
  }

}
