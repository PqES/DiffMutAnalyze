package org.ufla.dcc.equivalencyanalyse.model.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class ProjectGroupRelProject implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private ProjectGroupRelProjectId id = new ProjectGroupRelProjectId();

	@MapsId("projectId")
	@ManyToOne(fetch = FetchType.LAZY)
	private Project project;

	@MapsId("projectGroupId")
	@ManyToOne(fetch = FetchType.LAZY)
	private ProjectGroup projectGroup;

	public Project getProject() {
		return project;
	}

	public ProjectGroup getProjectGroup() {
		return projectGroup;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public void setProjectGroup(ProjectGroup projectGroup) {
		this.projectGroup = projectGroup;
	}

}
