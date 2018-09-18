package org.ufla.dcc.equivalencyanalyse.model.entity;

import org.springframework.data.annotation.CreatedDate;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
public class MutantReport implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(EnumType.ORDINAL)
  private Equivalence equivalence;

  @Enumerated(EnumType.ORDINAL)
  private Difficulty difficulty;

  private Long analysisTime;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn
  @JsonIgnore
  private MutantCode mutantCode;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn
  private User user;

  @CreatedDate
  @Column(nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date registrationDate;

  public Long getAnalysisTime() {
    return analysisTime;
  }

  public Difficulty getDifficulty() {
    return difficulty;
  }

  public Equivalence getEquivalence() {
    return equivalence;
  }

  public Long getId() {
    return id;
  }

  public MutantCode getMutantCode() {
    return mutantCode;
  }

  public Date getRegistrationDate() {
    return registrationDate;
  }

  public User getUser() {
    return user;
  }

  @PrePersist
  public void prePersist() {
    this.registrationDate = new Date();
  }

  public void setAnalysisTime(Long analysisTime) {
    this.analysisTime = analysisTime;
  }

  public void setDifficulty(Difficulty difficulty) {
    this.difficulty = difficulty;
  }

  public String getEquivalenceView() {
    if (equivalence == null) {
      return "-";
    }
    if (Equivalence.EQUIVALENT.equals(equivalence)) {
      return "Equivalente";
    } else {
      return "Não equivalente";
    }
  }

  public String getDifficultyView() {
    if (difficulty == null) {
      return "-";
    }
    return Integer.toString(difficulty.getValue());
  }

  public void setEquivalence(Equivalence equivalence) {
    this.equivalence = equivalence;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setMutantCode(MutantCode mutantCode) {
    this.mutantCode = mutantCode;
  }

  public void setRegistrationDate(Date registrationDate) {
    this.registrationDate = registrationDate;
  }

  public void setUser(User user) {
    this.user = user;
  }

}
