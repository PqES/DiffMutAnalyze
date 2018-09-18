package org.ufla.dcc.equivalencyanalyse.model;

import java.io.Serializable;

public class Message implements Serializable {

	private static final long serialVersionUID = 1L;

	private String content;
	private AlertType alertType;

	public Message() {

	}

	public Message(String content, AlertType alertType) {
		this.content = content;
		this.alertType = alertType;
	}

	public AlertType getAlertType() {
		return alertType;
	}

	public String getContent() {
		return content;
	}

	public boolean isNull() {
		return content == null && alertType == null;
	}

	public void setAlertType(AlertType alertType) {
		this.alertType = alertType;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "Mensagem [conteudo=" + content + ", tipoDeAlerta=" + alertType + "]";
	}

}
