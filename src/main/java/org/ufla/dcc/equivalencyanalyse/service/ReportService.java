package org.ufla.dcc.equivalencyanalyse.service;

import org.ufla.dcc.equivalencyanalyse.dto.report.ProjectReportStatisticsDto;
import org.ufla.dcc.equivalencyanalyse.dto.report.ReportDto;
import org.ufla.dcc.equivalencyanalyse.exception.NotAdminProjectException;
import org.ufla.dcc.equivalencyanalyse.model.entity.MutantReport;
import org.ufla.dcc.equivalencyanalyse.model.entity.User;

public interface ReportService {

  MutantReport save(ReportDto reportDto, User user) throws Exception;
  
  ProjectReportStatisticsDto getProjectReportStatistics(String projectHash, User user) throws NotAdminProjectException;

}
