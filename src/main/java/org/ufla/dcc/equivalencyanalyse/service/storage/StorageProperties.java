package org.ufla.dcc.equivalencyanalyse.service.storage;

import java.io.Serializable;

public class StorageProperties implements Serializable {

	private static final long serialVersionUID = 1L;

	private static StorageProperties storageProperties;

	public static StorageProperties getInstance() {
		if (storageProperties == null) {
			storageProperties = new StorageProperties();
		}
		return storageProperties;
	}

	private String uploadDirectory;

	private StorageProperties() {
		uploadDirectory = ".upload-dir";
	}

	public String getUploadDirectory() {
		return uploadDirectory;
	}

}
