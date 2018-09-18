package org.ufla.dcc.equivalencyanalyse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.ufla.dcc.equivalencyanalyse.model.entity.MutantCode;

public interface MutantCodeRepository extends JpaRepository<MutantCode, Long> {

}
