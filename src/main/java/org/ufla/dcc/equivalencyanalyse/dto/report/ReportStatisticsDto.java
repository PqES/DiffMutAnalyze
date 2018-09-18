package org.ufla.dcc.equivalencyanalyse.dto.report;

import java.util.HashSet;
import java.util.Set;
import org.ufla.dcc.equivalencyanalyse.model.entity.Difficulty;
import org.ufla.dcc.equivalencyanalyse.model.entity.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class ReportStatisticsDto {

  public long totalTime;

  public Set<User> persons = new HashSet<>();

  public int mutantReportQty;

  public long difficultySum;

  public int difficultyReportQty;

  public int[] countDifficulty = new int[5];

  public int equivalentQty;

  public int notEquivalentQty;

  public int deadMutantQty;

  public int aliveMutantQty;

  public int mutantEquivalentQty;

  public int mutantNotEquivalentQty;

  public String averageDifficulty() {
    if (mutantReportQty == 0) {
      return "-";
    }
    return Integer.toString(Math.round(difficultySum / (float) difficultyReportQty));
  }

  public String averageTimePerMutantReport() {
    if (mutantReportQty == 0) {
      return "-";
    }
    return Long.toString(totalTime / mutantReportQty);
  }

  public String averageTimePerPerson() {
    if (persons.size() == 0) {
      return "-";
    }
    return Long.toString(totalTime / persons.size());
  }

  public int getAliveMutantQty() {
    return aliveMutantQty;
  }

  public int[] getCountDifficulty() {
    return countDifficulty;
  }

  public int getDeadMutantQty() {
    return deadMutantQty;
  }

  public int getDifficultyReportQty() {
    return difficultyReportQty;
  }

  public long getDifficultySum() {
    return difficultySum;
  }

  public int getEquivalentQty() {
    return equivalentQty;
  }

  public int getMutantEquivalentQty() {
    return mutantEquivalentQty;
  }

  public int getMutantNotEquivalentQty() {
    return mutantNotEquivalentQty;
  }

  public int getMutantReportQty() {
    return mutantReportQty;
  }

  public int getNotEquivalentQty() {
    return notEquivalentQty;
  }

  public String difficultyDatasetJson() throws JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();
    return mapper.writeValueAsString(countDifficulty);
  }

  public String equivalenceDatasetJson() throws JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();
    return mapper.writeValueAsString(new int[] {equivalentQty, notEquivalentQty});
  }

  public String mutantsStatusDatasetJson() throws JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();
    return mapper.writeValueAsString(new int[] {aliveMutantQty, deadMutantQty});
  }

  public String mutantsEquivalenceDatasetJson() throws JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();
    return mapper.writeValueAsString(new int[] {mutantEquivalentQty, mutantNotEquivalentQty});
  }

  public Set<User> getPersons() {
    return persons;
  }

  public long getTotalTime() {
    return totalTime;
  }

  public void includeStatistics(ReportStatisticsDto reportStatistics) {
    totalTime += reportStatistics.totalTime;
    persons.addAll(reportStatistics.persons);
    mutantReportQty += reportStatistics.mutantReportQty;
    difficultySum += reportStatistics.difficultySum;
    difficultyReportQty += reportStatistics.difficultyReportQty;
    equivalentQty += reportStatistics.equivalentQty;
    notEquivalentQty += reportStatistics.notEquivalentQty;
    deadMutantQty += reportStatistics.deadMutantQty;
    aliveMutantQty += reportStatistics.aliveMutantQty;
    mutantEquivalentQty += reportStatistics.mutantEquivalentQty;
    mutantNotEquivalentQty += reportStatistics.mutantNotEquivalentQty;
    for (int index = 0; index < countDifficulty.length; index++) {
      countDifficulty[index] += reportStatistics.countDifficulty[index];
    }
  }

  public String mutationScore() {
    if (deadMutantQty + aliveMutantQty - mutantEquivalentQty == 0) {
      return "-";
    }
    return Double
        .toString(deadMutantQty / (double) (deadMutantQty + aliveMutantQty - mutantEquivalentQty));
  }

}
