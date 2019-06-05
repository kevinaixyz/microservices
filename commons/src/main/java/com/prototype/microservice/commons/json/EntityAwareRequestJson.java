package com.prototype.microservice.commons.json;

import java.util.Date;

/**
 * This is a request DTO that mimic the structure of an entity class with
 * standard set of fields (id, createBy, createDatetime, modifyBy,
 * modifyDatetime) which allows easy mapping with entity objects.
 * <p>
 * Do not extend from this DTO if your DTO object does not require any of these
 * entity object fields.
 */
@Deprecated
public class EntityAwareRequestJson extends RequestJson {

    private static final long serialVersionUID = -5831188488564057198L;
    private Long id;
    private String createBy;
    private Date createDatetime;
    private String modifyBy;
    private Date modifyDatetime;

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
