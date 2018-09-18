package org.ufla.dcc.equivalencyanalyse.service;

import org.ufla.dcc.equivalencyanalyse.model.entity.Project;

public interface GenerateMutantsService {

  void generateMutants(Project project) throws Exception;

}
