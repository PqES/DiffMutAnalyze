package org.ufla.dcc.equivalencyanalyse.model.entity;

import javax.persistence.*;
import java.io.Serializable;

@MappedSuperclass
public abstract class AbstractCode implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String hashkey;

	private String filename;

	private String filepath;

	public String getFilename() {
		return filename;
	}

	public String getFilepath() {
		return filepath;
	}

	public String getHashkey() {
		return hashkey;
	}

	public Long getId() {
		return id;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

	public void setHashkey(String hashkey) {
		this.hashkey = hashkey;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
