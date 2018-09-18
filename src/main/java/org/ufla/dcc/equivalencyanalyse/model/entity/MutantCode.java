package org.ufla.dcc.equivalencyanalyse.model.entity;

import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;

@Entity
public class MutantCode extends AbstractCode {

	private static final long serialVersionUID = 1L;

	private Boolean dead;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	@JsonIgnore
	private OriginalCode originalCode;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "mutantCode", cascade = CascadeType.REMOVE)
	@JsonIgnore
	private List<MutantReport> reports;

	@Override
	public String toString() {
		return "MutantCode " + getFilepath() + "";
	}

	public Boolean getDead() {
		return dead;
	}

	public OriginalCode getOriginalCode() {
		return originalCode;
	}

	public List<MutantReport> getReports() {
		return reports;
	}

	public void setDead(Boolean dead) {
		this.dead = dead;
	}

	public void setOriginalCode(OriginalCode originalCode) {
		this.originalCode = originalCode;
	}

	public void setReports(List<MutantReport> reports) {
		this.reports = reports;
	}

}
