package org.ufla.dcc.equivalencyanalyse.model.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class ProjectGroupAdmin implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private ProjectGroupAdminId id = new ProjectGroupAdminId();

	@MapsId("adminId")
	@ManyToOne(fetch = FetchType.LAZY)
	private User admin;

	@MapsId("projectGroupId")
	@ManyToOne(fetch = FetchType.LAZY)
	private ProjectGroup projectGroup;

	public User getAdmin() {
		return admin;
	}

	public ProjectGroup getProjectGroup() {
		return projectGroup;
	}

	public void setAdmin(User admin) {
		this.admin = admin;
	}

	public void setProjectGroup(ProjectGroup projectGroup) {
		this.projectGroup = projectGroup;
	}

}
