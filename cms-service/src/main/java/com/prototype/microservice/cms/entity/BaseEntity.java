package com.prototype.microservice.cms.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class BaseEntity {
    protected LocalDateTime createdDateTime;
    protected LocalDateTime lastUpdDateTime;
    protected String createdUser;
    protected String lastUpdUser;
    protected boolean isActive = true;
    protected long version;
}
