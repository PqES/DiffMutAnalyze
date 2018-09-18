package org.ufla.dcc.equivalencyanalyse.service;

import org.ufla.dcc.equivalencyanalyse.model.entity.Project;

public interface MutantsTestService {

	void testAllMutants(Project project) throws Exception;

}
