package com.prototype.microservice.edge.restful.dto;

import com.prototype.microservice.commons.json.SimpleResponseJson;

public class PasswordMatchingResponseDTO extends SimpleResponseJson {

    private Boolean matched;
    private String encoder;

    public PasswordMatchingResponseDTO(String instanceId) {
        super(instanceId);
    }

    public void setMatched(Boolean matched) {
        this.matched = matched;
    }

    public Boolean getMatched() {
        return matched;
    }

    public String getEncoder() {
        return encoder;
    }

    public void setEncoder(String encoder) {
        this.encoder = encoder;
    }

}
