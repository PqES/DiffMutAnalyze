package org.ufla.dcc.equivalencyanalyse.service;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import org.ufla.dcc.equivalencyanalyse.dto.projectinfo.ProjectInfo;
import org.ufla.dcc.equivalencyanalyse.model.entity.Project;
import org.ufla.dcc.equivalencyanalyse.model.entity.User;

public interface ProjectService {

  void registerProject(Project project, User user, int idTempDirectory, boolean registerGit);

  void registerProjectCompress(MultipartFile projectFile, int idTempDirectory);

  ProjectInfo getProjectInfo(User user, String projectHash);
  
  List<Project> myProjects(User user);

}
