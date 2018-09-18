package org.ufla.dcc.equivalencyanalyse.model.entity;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class ProjectAdminId implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long adminId;
	
	private Long projectId;

	public Long getAdminId() {
		return adminId;
	}

	public void setAdminId(Long adminId) {
		this.adminId = adminId;
	}

	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	
}
