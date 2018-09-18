package org.ufla.dcc.equivalencyanalyse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.ufla.dcc.equivalencyanalyse.model.entity.OriginalCode;

public interface OriginalCodeRepository extends JpaRepository<OriginalCode, Long> {

}
