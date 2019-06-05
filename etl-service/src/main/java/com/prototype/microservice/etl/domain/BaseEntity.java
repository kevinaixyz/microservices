package com.prototype.microservice.etl.domain;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;

public class BaseEntity implements java.io.Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1319861291187852173L;
    private String createBy;
    private Date createDatetime;
    private String modifyBy;
    private Date modifyDatetime;
    private Integer version;

    @Column(name = "CREATE_BY", nullable = false, length = 10)
    public String getCreateBy() {
        return this.createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATE_DATETIME", nullable = false, length = 19)
    public Date getCreateDatetime() {
        return this.createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        if (this.createDatetime == null) {
            this.createDatetime = createDatetime;
        }
    }

    @Column(name = "MODIFY_BY", nullable = false, length = 10)
    public String getModifyBy() {
        return this.modifyBy;
    }

    public void setModifyBy(String modifyBy) {
        this.modifyBy = modifyBy;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "MODIFY_DATETIME", nullable = false, length = 19)
    public Date getModifyDatetime() {
        return this.modifyDatetime;
    }

    public void setModifyDatetime(Date modifyDatetime) {
        this.modifyDatetime = modifyDatetime;
    }

    @Version
    @Column(name = "\"VERSION\"", nullable = false)
    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public void compareVersion(long version) {
        if (version < this.getVersion()) {
            throw new IllegalArgumentException("The 'name' parameter must not be null or empty");
        }
    }
}
