package org.ufla.dcc.equivalencyanalyse.dto.projectinfo;

import java.util.ArrayList;
import java.util.List;
import org.ufla.dcc.equivalencyanalyse.model.entity.MutantCode;
import org.ufla.dcc.equivalencyanalyse.model.entity.OriginalCode;

public class OriginalCodeInfo {
  
  private Long id;

  private String hashkey;

  private String filename;

  private String filepath;
  
  private List<MutantCodeInfo> mutantCodes;
  
  public OriginalCodeInfo() {

  }

  public OriginalCodeInfo(OriginalCode originalCode) {
    id = originalCode.getId();
    hashkey = originalCode.getHashkey();
    filename = originalCode.getFilename();
    filepath = originalCode.getFilepath();
    mutantCodes = new ArrayList<>();
    for (MutantCode mutantCode : originalCode.getMutantCodes()) {
      if (!mutantCode.getDead()) {
        mutantCodes.add(new MutantCodeInfo(mutantCode));
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

  public List<MutantCodeInfo> getMutantCodes() {
    return mutantCodes;
  }

  public void setMutantCodes(List<MutantCodeInfo> mutantCodes) {
    this.mutantCodes = mutantCodes;
  }

}
