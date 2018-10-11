package com.prototype.microservice.commons.json;

public class VersionAwareRequestJson extends RequestJson {

	private static final long serialVersionUID = -2327470948893632130L;
	private Integer version; // mandatory
	private String updateBy; // optional
	private String updateAt; // optional
	private String createBy; // optional
	private String createAt; // optional

	public VersionAwareRequestJson() {	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public String getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(String updateAt) {
		this.updateAt = updateAt;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public String getCreateAt() {
		return createAt;
	}

	public void setCreateAt(String createAt) {
		this.createAt = createAt;
	}

}
