package org.ufla.dcc.equivalencyanalyse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.ufla.dcc.equivalencyanalyse.model.entity.ProjectAdmin;
import org.ufla.dcc.equivalencyanalyse.model.entity.ProjectAdminId;

public interface ProjectAdminRepository extends JpaRepository<ProjectAdmin, ProjectAdminId> {
  
  

}
