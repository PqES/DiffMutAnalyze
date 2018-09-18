package org.ufla.dcc.equivalencyanalyse.dto.projectinfo;

import java.util.ArrayList;
import java.util.List;
import org.ufla.dcc.equivalencyanalyse.model.entity.OriginalCode;
import org.ufla.dcc.equivalencyanalyse.model.entity.Project;

public class ProjectInfo {

  private Long id;

  private String hashkey;

  private String name;

  private String directorySource;

  private List<OriginalCodeInfo> originalCodes;

  public ProjectInfo() {

  }

  public ProjectInfo(Project project) {
    id = project.getId();
    hashkey = project.getHashkey();
    name = project.getName();
    directorySource = project.getDirectorySource();
    originalCodes = new ArrayList<>();
    for (OriginalCode originalCode : project.getOriginalCodes()) {
      OriginalCodeInfo originalCodeInfo = new OriginalCodeInfo(originalCode);
      if (!originalCodeInfo.getMutantCodes().isEmpty()) {
        originalCodes.add(originalCodeInfo);
      }
    }
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

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDirectorySource() {
    return directorySource;
  }

  public void setDirectorySource(String directorySource) {
    this.directorySource = directorySource;
  }

  public List<OriginalCodeInfo> getOriginalCodes() {
    return originalCodes;
  }

  public void setOriginalCodes(List<OriginalCodeInfo> originalCodes) {
    this.originalCodes = originalCodes;
  }


}
