package org.ufla.dcc.equivalencyanalyse.service.mail;

import java.io.IOException;

import javax.mail.MessagingException;

import org.ufla.dcc.equivalencyanalyse.model.entity.Project;

public interface MailerService {

	void sendEmailRegisterProject(Project project) throws MessagingException, IOException;

	void sendEmailRegisterProjectError(Project project, String errorMessage) throws MessagingException, IOException;

}
