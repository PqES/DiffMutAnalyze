package org.ufla.dcc.equivalencyanalyse.dto.report;

import org.ufla.dcc.equivalencyanalyse.model.entity.Difficulty;
import org.ufla.dcc.equivalencyanalyse.model.entity.Equivalence;
import org.ufla.dcc.equivalencyanalyse.model.entity.MutantReport;

public class ReportDto {

  private Long id;

  private Equivalence equivalence;

  private Difficulty difficulty;

  private Long analysisTime;

  private Long mutantCodeId;

  public MutantReport toMutantReport() {
    MutantReport mutantReport = new MutantReport();
    mutantReport.setId(id);
    mutantReport.setEquivalence(equivalence);
    mutantReport.setDifficulty(difficulty);
    mutantReport.setAnalysisTime(analysisTime);
    return mutantReport;
  }

  public void injectData(MutantReport mutantReport) {
    mutantReport.setEquivalence(equivalence);
    mutantReport.setDifficulty(difficulty);
    mutantReport.setAnalysisTime(analysisTime);
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

  public String getEquivalenceView() {
    if (equivalence == null) {
      return "-";
    }
    if (Equivalence.EQUIVALENT.equals(equivalence)) {
      return "Equivalente";
    } else {
      return "Não equivalente";
    }
  }

  public String getDifficultyView() {
    if (difficulty == null) {
      return "-";
    }
    return Integer.toString(difficulty.getValue());
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

  public Long getMutantCodeId() {
    return mutantCodeId;
  }

  public void setMutantCodeId(Long mutantCodeId) {
    this.mutantCodeId = mutantCodeId;
  }

}
