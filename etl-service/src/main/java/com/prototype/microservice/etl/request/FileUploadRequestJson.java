package com.prototype.microservice.etl.request;


public class FileUploadRequestJson   extends BaseRequestJson {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String file;
	private String fileName;
	
	public String getFile() {
		return file;
	}
	public void setFile(String file) {
		this.file = file;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}
