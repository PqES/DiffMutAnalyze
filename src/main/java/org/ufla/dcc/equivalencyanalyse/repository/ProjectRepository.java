package org.ufla.dcc.equivalencyanalyse.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.ufla.dcc.equivalencyanalyse.model.entity.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {

  Project findByHashkey(String hashkey);

  @Query("SELECT project FROM Project project JOIN project.projectAdmins projectAdmin where projectAdmin.admin.id = :id")
  List<Project> findMyManagedProjects(@Param("id") Long id);
  
  @Query("SELECT project FROM Project project WHERE project.owner.id = :id")
  List<Project> findMyProjects(@Param("id") Long id);

}
