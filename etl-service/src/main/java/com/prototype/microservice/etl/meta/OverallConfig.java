package com.prototype.microservice.etl.meta;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class OverallConfig {
	List<CommonConfigInfo> configList;

	public List<CommonConfigInfo> getConfigList() {
		return configList;
	}

	public void setConfigList(List<CommonConfigInfo> configList) {
		this.configList = configList;
	}
}
