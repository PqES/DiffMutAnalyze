package org.ufla.dcc.equivalencyanalyse.model.entity;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class ProjectGroupRelProjectId implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long projectId;

	private Long projectGroupId;

	public Long getProjectGroupId() {
		return projectGroupId;
	}

	public Long getProjectId() {
		return projectId;
	}

	public void setProjectGroupId(Long projectGroupId) {
		this.projectGroupId = projectGroupId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

}
