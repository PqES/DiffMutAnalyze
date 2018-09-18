package org.ufla.dcc.equivalencyanalyse.dto.report;

import java.util.Map;
import org.ufla.dcc.equivalencyanalyse.model.entity.Equivalence;
import org.ufla.dcc.equivalencyanalyse.model.entity.MutantCode;
import org.ufla.dcc.equivalencyanalyse.model.entity.MutantReport;
import org.ufla.dcc.equivalencyanalyse.model.entity.OriginalCode;
import org.ufla.dcc.equivalencyanalyse.model.entity.User;

public class OriginalCodeReportStatisticsDto extends ReportStatisticsDto {

  public OriginalCode originalCode;

  public OriginalCode getOriginalCode() {
    return originalCode;
  }

  public OriginalCodeReportStatisticsDto(OriginalCode originalCode, Map<User, PersonReportDto> personToReport) {
    this.originalCode = originalCode;
    defineStatistics(personToReport);
  }

  private void defineStatistics(Map<User, PersonReportDto> personToReport) {
    if (originalCode.getMutantCodes() == null) {
      return;
    }
    for (MutantCode mutantCode : originalCode.getMutantCodes()) {
      if (mutantCode.getDead()) {
        deadMutantQty++;
        continue;
      }
      aliveMutantQty++;
      if (mutantCode.getReports() == null) {
        continue;
      }
      int lastEquivalentQty = equivalentQty;
      int lastNotEquivalentQty = notEquivalentQty;
      for (MutantReport report : mutantCode.getReports()) {
        if (report.getEquivalence() == null) {
          continue;
        }
        if (report.getEquivalence().equals(Equivalence.EQUIVALENT)) {
          equivalentQty++;
        } else {
          notEquivalentQty++;
        }
        if (report.getDifficulty() != null) {
          int difficulty = report.getDifficulty().getValue();
          difficultySum += difficulty;
          countDifficulty[difficulty - 1]++;
          difficultyReportQty++;
        }
        totalTime += report.getAnalysisTime();
        mutantReportQty++;
        User user = report.getUser();
        persons.add(user);
        if (!personToReport.containsKey(user)) {
          personToReport.put(user, new PersonReportDto(user));
        }
        personToReport.get(user).reports.add(report);
      }
      int actualEquivalentQty = equivalentQty - lastEquivalentQty;
      int actualNotEquivalentQty = notEquivalentQty - lastNotEquivalentQty;
      if (actualEquivalentQty + actualNotEquivalentQty == 0) {
        continue;
      }
      if (actualEquivalentQty >= actualNotEquivalentQty) {
        mutantEquivalentQty++;
      } else {
        mutantNotEquivalentQty++;
      }
    }
  }


}
