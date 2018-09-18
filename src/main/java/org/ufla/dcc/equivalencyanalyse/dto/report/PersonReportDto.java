package org.ufla.dcc.equivalencyanalyse.dto.report;

import java.util.ArrayList;
import java.util.List;
import org.ufla.dcc.equivalencyanalyse.model.entity.MutantReport;
import org.ufla.dcc.equivalencyanalyse.model.entity.User;

public class PersonReportDto {

  public User person;

  public List<MutantReport> reports = new ArrayList<>();

  public PersonReportDto(User person) {
    this.person = person;
  }

  public User getPerson() {
    return person;
  }

  public List<MutantReport> getReports() {
    return reports;
  }

}
