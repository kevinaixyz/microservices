package com.prototype.microservice.commons.json;

import java.util.Date;

/**
 * This is a response DTO that mimic the structure of an entity class with
 * standard set of fields (id, createBy, createDatetime, modifyBy,
 * modifyDatetime) which allows easy mapping with entity objects.
 *
 * Do not extend from this DTO if your DTO object does not require any of these
 * entity object fields.
 *
 *
 *
 */
@Deprecated
public class EntityAwareResponseJson extends SimpleResponseJson {

	private static final long serialVersionUID = -308733180970464779L;
	private Long id;
	private String createBy;
	private Date createDatetime;
	private String modifyBy;
	private Date modifyDatetime;

	public EntityAwareResponseJson(final String instanceId) {
		super(instanceId);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public Date getCreateDatetime() {
		return createDatetime;
	}

	public void setCreateDatetime(Date createDatetime) {
		this.createDatetime = createDatetime;
	}

	public String getModifyBy() {
		return modifyBy;
	}

	public void setModifyBy(String modifyBy) {
		this.modifyBy = modifyBy;
	}

	public Date getModifyDatetime() {
		return modifyDatetime;
	}

	public void setModifyDatetime(Date modifyDatetime) {
		this.modifyDatetime = modifyDatetime;
	}

}
