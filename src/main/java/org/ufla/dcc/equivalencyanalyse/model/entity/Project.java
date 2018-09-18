package org.ufla.dcc.equivalencyanalyse.model.entity;

import javax.persistence.*;
import java.util.List;

@Entity
public class Project extends AbstractProject {

	private static final long serialVersionUID = 1L;

	private String gitURL;

	private String directorySource;

	@Column(nullable = false)
	private Boolean withCode;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "project", cascade = CascadeType.REMOVE)
	private List<OriginalCode> originalCodes;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "project", cascade = CascadeType.REMOVE)
	private List<ProjectAdmin> projectAdmins;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "project", cascade = CascadeType.REMOVE)
	private List<ProjectGroupRelProject> projectsGroup;

	public List<ProjectAdmin> getProjectAdmins() {
		return projectAdmins;
	}

	public String getDirectorySource() {
		return directorySource;
	}

	public String getGitURL() {
		return gitURL;
	}

	public List<OriginalCode> getOriginalCodes() {
		return originalCodes;
	}

	public List<ProjectGroupRelProject> getProjectsGroup() {
		return projectsGroup;
	}

	public Boolean getWithCode() {
		return withCode;
	}

	public void setProjectAdmins(List<ProjectAdmin> projectAdmins) {
		this.projectAdmins = projectAdmins;
	}

	public void setDirectorySource(String directorySource) {
		this.directorySource = directorySource;
	}

	public void setGitURL(String gitURL) {
		this.gitURL = gitURL;
	}

	public void setOriginalCodes(List<OriginalCode> originalCodes) {
		this.originalCodes = originalCodes;
	}

	public void setProjectsGroup(List<ProjectGroupRelProject> projectsGroup) {
		this.projectsGroup = projectsGroup;
	}

	public void setWithCode(Boolean withCode) {
		this.withCode = withCode;
	}
}
