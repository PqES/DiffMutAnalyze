package org.ufla.dcc.equivalencyanalyse.model.entity;

import javax.persistence.*;
import java.util.List;

@Entity
public class ProjectGroup extends AbstractProject {

	private static final long serialVersionUID = 1L;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "projectGroup")
	private List<ProjectGroupAdmin> projectGroupAdmins;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "projectGroup")
	private List<ProjectGroupRelProject> projects;

	public List<ProjectGroupAdmin> getProjectGroupAdmins() {
		return projectGroupAdmins;
	}

	public List<ProjectGroupRelProject> getProjects() {
		return projects;
	}

	public void setProjectGroupAdmins(List<ProjectGroupAdmin> admins) {
		this.projectGroupAdmins = admins;
	}

	public void setProjects(List<ProjectGroupRelProject> projects) {
		this.projects = projects;
	}

}
