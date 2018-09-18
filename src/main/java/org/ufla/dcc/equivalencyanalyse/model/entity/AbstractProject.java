package org.ufla.dcc.equivalencyanalyse.model.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

@MappedSuperclass
public abstract class AbstractProject implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String hashkey;

	@Column(nullable = false)
	private String name;

	@CreatedBy
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn
	private User owner;

	@CreatedDate
	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationDate;

	public Date getCreationDate() {
		return creationDate;
	}

	public String getHashkey() {
		return hashkey;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public User getOwner() {
		return owner;
	}

	@PrePersist
	public void prePersist() {
		this.creationDate = new Date();
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public void setHashkey(String hashkey) {
		this.hashkey = hashkey;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

}
