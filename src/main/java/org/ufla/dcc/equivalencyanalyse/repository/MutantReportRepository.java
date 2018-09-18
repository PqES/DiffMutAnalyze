package org.ufla.dcc.equivalencyanalyse.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.ufla.dcc.equivalencyanalyse.model.entity.MutantReport;

public interface MutantReportRepository extends JpaRepository<MutantReport, Long> {

  @Query("SELECT report FROM MutantReport report WHERE report.user.id = ?1 and report.mutantCode.id = ?2")
  Optional<MutantReport> findReportByUserAndMutant(Long userId, Long mutantId);

}

