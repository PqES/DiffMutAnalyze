package org.ufla.dcc.equivalencyanalyse.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.ufla.dcc.equivalencyanalyse.dto.projectinfo.ProjectInfo;
import org.ufla.dcc.equivalencyanalyse.model.AlertType;
import org.ufla.dcc.equivalencyanalyse.model.Message;
import org.ufla.dcc.equivalencyanalyse.model.entity.Project;
import org.ufla.dcc.equivalencyanalyse.model.entity.User;
import org.ufla.dcc.equivalencyanalyse.service.ProjectService;
import org.ufla.dcc.equivalencyanalyse.service.ProjectServiceImpl;
import org.ufla.dcc.equivalencyanalyse.service.storage.FileStorageService;

@Controller
@RequestMapping("/project")
public class ProjectController {

  @Autowired
  private ProjectService projectService;

  @Autowired
  private FileStorageService fileStorageService;


  @GetMapping("/analyze/{projectHash}")
  public String getAnalyzeProject(@PathVariable String projectHash, HttpSession session) {
    if (session.getAttribute("user") == null) {
      return "redirect:/user/login";
    }
    return "project/analyze-project";
  }

  @GetMapping("/info/{projectHash}")
  @ResponseBody
  public ResponseEntity<ProjectInfo> getProjectInfo(@PathVariable String projectHash,
      HttpSession session) {
    User user = (User) session.getAttribute("user");
    if (user == null) {
      return new ResponseEntity<ProjectInfo>(new ProjectInfo(), HttpStatus.UNAUTHORIZED);
    }
    ProjectInfo projectInfo = projectService.getProjectInfo(user, projectHash);
    return new ResponseEntity<ProjectInfo>(projectInfo, HttpStatus.OK);
  }

  @GetMapping("/file/{filepath}")
  @ResponseBody
  public ResponseEntity<String> getFile(@PathVariable String filepath, HttpSession session) {
    if (session.getAttribute("user") == null) {
      return new ResponseEntity<String>("", HttpStatus.UNAUTHORIZED);
    }
    filepath = filepath.replaceAll("@sep@", "/");
    try {
      return new ResponseEntity<String>(fileStorageService.loadContent(filepath), HttpStatus.OK);
    } catch (IOException e) {
      e.printStackTrace();
      return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/my")
  public String getMy(Model theModel, HttpSession session) {
    User user = (User) session.getAttribute("user");
    if (user == null) {
      return "redirect:/user/login";
    }
    List<Project> myProjects = projectService.myProjects(user);
    theModel.addAttribute("myProjects", myProjects);
    return "project/my-projects";
  }


  @GetMapping("/register")
  public String getRegister(Model theModel, @ModelAttribute("project") Project project,
      HttpSession session) {
    if (session.getAttribute("user") == null) {
      return "redirect:/user/login";
    }
    return "project/project-register";
  }

  @PostMapping("/register")
  public String postRegister(Model theModel, @ModelAttribute("project") Project project,
      @RequestParam("projectFile") MultipartFile projectFile, RedirectAttributes attributes,
      HttpSession session) {
    User user = (User) session.getAttribute("user");
    if (user == null) {
      return "redirect:/user/login";
    }
    int idTempDirectory = Math.abs(new Random().nextInt());
    File directory = new File(ProjectServiceImpl.getCompleteTempDirectory(idTempDirectory));
    if (!directory.mkdirs()) {
      attributes.addFlashAttribute("message",
          new Message(String.format("Não foi possível criar o diretório %s do projeto!",
              directory.getAbsolutePath()), AlertType.DANGER));
      return "redirect:/user/home";
    }
    boolean registerGit = true;
    if (!projectFile.isEmpty()) {
      projectService.registerProjectCompress(projectFile, idTempDirectory);
      registerGit = false;
    }
    projectService.registerProject(project, user, idTempDirectory, registerGit);
    attributes.addFlashAttribute("message",
        new Message(
            "Projeto está sendo cadastro. As informações do cadastro serão informadas via email.",
            AlertType.INFO));
    return "redirect:/user/home";
  }

}
