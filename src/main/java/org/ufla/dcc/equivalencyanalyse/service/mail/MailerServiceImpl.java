package org.ufla.dcc.equivalencyanalyse.service.mail;

import static org.ufla.dcc.equivalencyanalyse.config.resources.RESOURCES.REGISTER_PROJECT_EMAIL_RESOURCE;
import static org.ufla.dcc.equivalencyanalyse.config.resources.RESOURCES.REGISTER_PROJECT_ERROR_EMAIL_RESOURCE;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.ufla.dcc.equivalencyanalyse.model.entity.Project;

@Service
public class MailerServiceImpl implements MailerService {

	private static final String REGISTER_PROJECT_EMAIL_SUBJECT = "Registro de Projeto";
	private static final String FROM_EMAIL = "diffMutant@gmail.com";
	private static final String FROM_NAME = "Diff Mutant Analyze";
	private static final String EMAIL_ENCODING = "utf-8";
	private static final String EMAIL_CONTENT_TYPE = "text/html; charset=UTF-8";

	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	private ResourceLoader resourceLoader;

	private void sendMail(String fromEmail, String fromName, String to, String subject, String contentHtml)
			throws MessagingException, UnsupportedEncodingException {
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, EMAIL_ENCODING);
		helper.setFrom(fromEmail, fromName);
		helper.setTo(to);
		helper.setSubject(subject);
		mimeMessage.setContent(contentHtml, EMAIL_CONTENT_TYPE);
		javaMailSender.send(mimeMessage);
	}

	@Override
	public void sendEmailRegisterProject(Project project) throws MessagingException, IOException {
		System.out.println("ENVIANDO EMAIL para " + project.getOwner().getEmail() + "...");
		Resource resource = resourceLoader.getResource(REGISTER_PROJECT_EMAIL_RESOURCE);
		byte[] encodedHtmlEmailContent = new byte[(int) resource.contentLength()];
		resource.getInputStream().read(encodedHtmlEmailContent);
		String htmlEmailContent = new String(encodedHtmlEmailContent);
		sendMail(FROM_EMAIL, FROM_NAME, project.getOwner().getEmail(), REGISTER_PROJECT_EMAIL_SUBJECT, MessageFormat
				.format(htmlEmailContent, project.getOwner().getName(), project.getName(), project.getHashkey()));
		System.out.println("EMAIL ENVIADO com sucesso");

	}

	@Override
	public void sendEmailRegisterProjectError(Project project, String errorMessage)
			throws MessagingException, IOException {
		System.out.println("ENVIANDO EMAIL para " + project.getOwner().getEmail() + "...");
		Resource resource = resourceLoader.getResource(REGISTER_PROJECT_ERROR_EMAIL_RESOURCE);
		byte[] encodedHtmlEmailContent = new byte[(int) resource.contentLength()];
		resource.getInputStream().read(encodedHtmlEmailContent);
		String htmlEmailContent = new String(encodedHtmlEmailContent);
		sendMail(FROM_EMAIL, FROM_NAME, project.getOwner().getEmail(), REGISTER_PROJECT_EMAIL_SUBJECT,
				MessageFormat.format(htmlEmailContent, project.getOwner().getName(), project.getName(), errorMessage));
		System.out.println("EMAIL ENVIADO com sucesso");
	}

}