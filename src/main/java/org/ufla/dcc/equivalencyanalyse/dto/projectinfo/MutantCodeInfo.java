package org.ufla.dcc.equivalencyanalyse.dto.projectinfo;

import org.ufla.dcc.equivalencyanalyse.model.entity.MutantCode;

public class MutantCodeInfo {

  private Long id;

  private String hashkey;

  private String filename;

  private String filepath;

  private MutantReportInfo report;

  public MutantCodeInfo() {

  }

  public MutantCodeInfo(MutantCode mutantCode) {
    id = mutantCode.getId();
    hashkey = mutantCode.getHashkey();
    filename = mutantCode.getFilename();
    filepath = mutantCode.getFilepath();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getHashkey() {
    return hashkey;
  }

  public void setHashkey(String hashkey) {
    this.hashkey = hashkey;
  }

  public String getFilename() {
    return filename;
  }

  public void setFilename(String filename) {
    this.filename = filename;
  }

  public String getFilepath() {
    return filepath;
  }

  public void setFilepath(String filepath) {
    this.filepath = filepath;
  }

  public MutantReportInfo getReport() {
    return report;
  }

  public void setReport(MutantReportInfo report) {
    this.report = report;
  }

}
