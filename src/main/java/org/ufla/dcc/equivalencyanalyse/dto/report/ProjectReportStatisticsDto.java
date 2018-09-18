package org.ufla.dcc.equivalencyanalyse.dto.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.ufla.dcc.equivalencyanalyse.model.entity.OriginalCode;
import org.ufla.dcc.equivalencyanalyse.model.entity.Project;
import org.ufla.dcc.equivalencyanalyse.model.entity.User;

public class ProjectReportStatisticsDto extends ReportStatisticsDto {

  public Project project;

  public int originalCodeQty;

  public List<OriginalCodeReportStatisticsDto> originalCodesStatistics;
  
  public Map<User, PersonReportDto> personToReport;

  public ProjectReportStatisticsDto(Project project) {
    this.project = project;
    originalCodesStatistics = new ArrayList<>();
    personToReport = new HashMap<>();
    defineStatistics();
  }

  public Project getProject() {
    return project;
  }

  public int getOriginalCodeQty() {
    return originalCodeQty;
  }

  public List<OriginalCodeReportStatisticsDto> getOriginalCodesStatistics() {
    return originalCodesStatistics;
  }

  public Map<User, PersonReportDto> getPersonToReport() {
    return personToReport;
  }

  private void defineStatistics() {
    if (project.getOriginalCodes() == null) {
      return;
    }
    for (OriginalCode originalCode : project.getOriginalCodes()) {
      OriginalCodeReportStatisticsDto originalCodeReportStatistics =
          new OriginalCodeReportStatisticsDto(originalCode, personToReport);
      if (originalCodeReportStatistics.mutantReportQty > 0) {
        originalCodeQty++;
      }
      originalCodesStatistics.add(originalCodeReportStatistics);
      includeStatistics(originalCodeReportStatistics);
      
    }
  }

  public double averageTimePerOriginalCode() {
    return totalTime / originalCodeQty;
  }

}
