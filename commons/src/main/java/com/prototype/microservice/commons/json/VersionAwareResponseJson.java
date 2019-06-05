package com.prototype.microservice.commons.json;

public class VersionAwareResponseJson extends SimpleResponseJson {

    private static final long serialVersionUID = -2855927913634754677L;
    private Integer version; // mandatory
    private String updateBy; // optional
    private String updateAt; // optional
    private String createBy; // optional
    private String createAt; // optional

    public VersionAwareResponseJson() {
        super();
    }

    public VersionAwareResponseJson(final String instanceId) {
        super(instanceId);
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(String updateAt) {
        this.updateAt = updateAt;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
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
