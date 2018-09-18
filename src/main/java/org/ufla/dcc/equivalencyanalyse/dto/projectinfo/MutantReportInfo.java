package org.ufla.dcc.equivalencyanalyse.dto.projectinfo;

import org.ufla.dcc.equivalencyanalyse.model.entity.Difficulty;
import org.ufla.dcc.equivalencyanalyse.model.entity.Equivalence;
import org.ufla.dcc.equivalencyanalyse.model.entity.MutantReport;

public class MutantReportInfo {

  private Long id;

  private Equivalence equivalence;

  private Difficulty difficulty;

  private Long analysisTime;

  public MutantReportInfo() {

  }

  public MutantReportInfo(MutantReport mutantReport) {
    if (mutantReport == null) {
      return;
    }
    id = mutantReport.getId();
    equivalence = mutantReport.getEquivalence();
    difficulty = mutantReport.getDifficulty();
    analysisTime = mutantReport.getAnalysisTime();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Equivalence getEquivalence() {
    return equivalence;
  }

  public void setEquivalence(Equivalence equivalence) {
    this.equivalence = equivalence;
  }

  public Difficulty getDifficulty() {
    return difficulty;
  }

  public void setDifficulty(Difficulty difficulty) {
    this.difficulty = difficulty;
  }

  public Long getAnalysisTime() {
    return analysisTime;
  }

  public void setAnalysisTime(Long analysisTime) {
    this.analysisTime = analysisTime;
  }

}
