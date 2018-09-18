package org.ufla.dcc.equivalencyanalyse.model;

import java.io.Serializable;

public enum AlertType implements Serializable {

	DEFAULT(0, ""), SUCCESS(1, "alert-success"), INFO(2, "alert-info"), WARNING(3, "alert-warning"), DANGER(4,
			"alert-danger");

	private Integer id;
	private String name;

	AlertType(Integer id, String name) {
		this.id = id;
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String toString() {
		return name;
	}

}
