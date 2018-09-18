package org.ufla.dcc.equivalencyanalyse.model.entity;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class ProjectGroupAdminId implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long adminId;

	private Long projectGroupId;

	public Long getAdminId() {
		return adminId;
	}

	public Long getProjectGroupId() {
		return projectGroupId;
	}

	public void setAdminId(Long adminId) {
		this.adminId = adminId;
	}

	public void setProjectGroupId(Long projectGroupId) {
		this.projectGroupId = projectGroupId;
	}

}
