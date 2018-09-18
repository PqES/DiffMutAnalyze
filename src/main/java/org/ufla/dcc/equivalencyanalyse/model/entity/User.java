package org.ufla.dcc.equivalencyanalyse.model.entity;

import org.springframework.data.annotation.CreatedDate;
import org.ufla.dcc.equivalencyanalyse.model.AbstractPassword;
import org.ufla.dcc.equivalencyanalyse.model.Password;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
public class User extends AbstractPassword implements Serializable, Password {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false, unique = true)
	private String email;

	@Column(nullable = false)
	private String password;

	@CreatedDate
	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date registrationDate;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
	@JsonIgnore
	private List<MutantReport> reports;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "owner")
	@JsonIgnore
	private List<Project> myProjects;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "admin")
	@JsonIgnore
	private List<ProjectAdmin> managedProjects;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "owner")
	@JsonIgnore
	private List<ProjectGroup> myProjectsGroup;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "admin")
	@JsonIgnore
	private List<ProjectGroupAdmin> managedProjectsGroup;

	public String getEmail() {
		return email;
	}

	public Long getId() {
		return id;
	}

	public List<ProjectAdmin> getManagedProjects() {
		return managedProjects;
	}

	public List<ProjectGroupAdmin> getManagedProjectsGroup() {
		return managedProjectsGroup;
	}

	public List<Project> getMyProjects() {
		return myProjects;
	}

	public List<ProjectGroup> getMyProjectsGroup() {
		return myProjectsGroup;
	}

	public String getName() {
		return name;
	}

	public String getPassword() {
		return password;
	}

	public Date getRegistrationDate() {
		return registrationDate;
	}

	public List<MutantReport> getReports() {
		return reports;
	}

	@PrePersist
	public void prePersist() {
		this.registrationDate = new Date();
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setManagedProjects(List<ProjectAdmin> managedProjects) {
		this.managedProjects = managedProjects;
	}

	public void setManagedProjectsGroup(List<ProjectGroupAdmin> managedProjectsGroup) {
		this.managedProjectsGroup = managedProjectsGroup;
	}

	public void setMyProjects(List<Project> myProjects) {
		this.myProjects = myProjects;
	}

	public void setMyProjectsGroup(List<ProjectGroup> myProjectsGroup) {
		this.myProjectsGroup = myProjectsGroup;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}

	public void setReports(List<MutantReport> reports) {
		this.reports = reports;
	}

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((email == null) ? 0 : email.hashCode());
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    result = prime * result + ((password == null) ? 0 : password.hashCode());
    result = prime * result + ((registrationDate == null) ? 0 : registrationDate.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    User other = (User) obj;
    if (email == null) {
      if (other.email != null)
        return false;
    } else if (!email.equals(other.email))
      return false;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    if (name == null) {
      if (other.name != null)
        return false;
    } else if (!name.equals(other.name))
      return false;
    if (password == null) {
      if (other.password != null)
        return false;
    } else if (!password.equals(other.password))
      return false;
    if (registrationDate == null) {
      if (other.registrationDate != null)
        return false;
    } else if (!registrationDate.equals(other.registrationDate))
      return false;
    return true;
  }
}