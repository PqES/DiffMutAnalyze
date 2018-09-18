package org.ufla.dcc.equivalencyanalyse.model.entity;

import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;

@Entity
public class OriginalCode extends AbstractCode {

	private static final long serialVersionUID = 1L;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "originalCode", cascade = CascadeType.REMOVE)
	private List<MutantCode> mutantCodes;

	@Override
	public String toString() {
		return "OriginalCode " + getFilepath() + "";
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	@JsonIgnore
	private Project project;

	public void addMutantCode(MutantCode mutantCode) {
		if (this.mutantCodes == null) {
			this.mutantCodes = new ArrayList<MutantCode>();
		}
		this.mutantCodes.add(mutantCode);
		mutantCode.setOriginalCode(this);
	}

	public List<MutantCode> getMutantCodes() {
		return mutantCodes;
	}

	public Project getProject() {
		return project;
	}

	public void setMutantCodes(List<MutantCode> mutantCodes) {
		this.mutantCodes = mutantCodes;
	}

	public void setProject(Project project) {
		this.project = project;
	}

}
