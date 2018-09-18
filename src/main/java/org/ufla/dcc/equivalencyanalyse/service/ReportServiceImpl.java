package org.ufla.dcc.equivalencyanalyse.service;

import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ufla.dcc.equivalencyanalyse.dto.report.ProjectReportStatisticsDto;
import org.ufla.dcc.equivalencyanalyse.dto.report.ReportDto;
import org.ufla.dcc.equivalencyanalyse.exception.NotAdminProjectException;
import org.ufla.dcc.equivalencyanalyse.exception.NotAuthorException;
import org.ufla.dcc.equivalencyanalyse.model.entity.MutantCode;
import org.ufla.dcc.equivalencyanalyse.model.entity.MutantReport;
import org.ufla.dcc.equivalencyanalyse.model.entity.Project;
import org.ufla.dcc.equivalencyanalyse.model.entity.ProjectAdmin;
import org.ufla.dcc.equivalencyanalyse.model.entity.User;
import org.ufla.dcc.equivalencyanalyse.repository.MutantCodeRepository;
import org.ufla.dcc.equivalencyanalyse.repository.MutantReportRepository;
import org.ufla.dcc.equivalencyanalyse.repository.ProjectRepository;

@Service
public class ReportServiceImpl implements ReportService {

  @Autowired
  private MutantReportRepository mutantReportRepository;

  @Autowired
  private MutantCodeRepository mutantCodeRepository;
  
  @Autowired
  private ProjectRepository projectRepository;

  @Override
  @Transactional
  public MutantReport save(ReportDto reportDto, User user) throws Exception {
    MutantReport mutantReport;
    if (reportDto.getId() == null) {
      mutantReport = reportDto.toMutantReport();
      Optional<MutantCode> opMutantCode =
          mutantCodeRepository.findById(reportDto.getMutantCodeId());
      if (opMutantCode.isPresent()) {
        mutantReport.setMutantCode(opMutantCode.get());
        mutantReport.setUser(user);
      } else {
        throw new Exception(
            "Id inválido do código do mutante '" + reportDto.getMutantCodeId() + "'!");
      }
    } else {
      Optional<MutantReport> opMutantReport = mutantReportRepository.findById(reportDto.getId());
      if (opMutantReport.isPresent()) {
        mutantReport = opMutantReport.get();
        reportDto.injectData(mutantReport);
        User userReport = mutantReport.getUser();
        if (!userReport.equals(user)) {
          throw new NotAuthorException();
        }
      } else {
        throw new Exception("Id inválido do relatório do mutante '" + reportDto.getId() + "'!");
      }
    }
    mutantReportRepository.save(mutantReport);
    return mutantReport;
  }
  
  private boolean isAdminProject(Project project, User user) {
    if (project.getOwner().equals(user)) {
      return true;
    }
    if (project.getProjectAdmins() == null) {
      return false;
    }
    for (ProjectAdmin projectAdmin : project.getProjectAdmins()) {
      if (projectAdmin.getAdmin().equals(user)) {
        return true;
      }
    }
    return false;
  }

  @Override
  @Transactional
  public ProjectReportStatisticsDto getProjectReportStatistics(String projectHash, User user)
      throws NotAdminProjectException {
    Project project = projectRepository.findByHashkey(projectHash);
    if (!isAdminProject(project, user)) {
      throw new NotAdminProjectException();
    }
    ProjectReportStatisticsDto projectReportStatisticsDto = new ProjectReportStatisticsDto(project);
    return projectReportStatisticsDto;
  }

}
