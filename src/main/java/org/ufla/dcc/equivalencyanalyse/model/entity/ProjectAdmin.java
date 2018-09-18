package org.ufla.dcc.equivalencyanalyse.model.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class ProjectAdmin implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private ProjectAdminId id = new ProjectAdminId();

	@MapsId("adminId")
	@ManyToOne(fetch = FetchType.LAZY)
	private User admin;

	@MapsId("projectId")
	@ManyToOne(fetch = FetchType.LAZY)
	private Project project;

	public User getAdmin() {
		return admin;
	}

	public ProjectAdminId getId() {
		return id;
	}

	public Project getProject() {
		return project;
	}

	public void setAdmin(User admin) {
		this.admin = admin;
	}

	public void setId(ProjectAdminId id) {
		this.id = id;
	}

	public void setProject(Project project) {
		this.project = project;
	}

}
