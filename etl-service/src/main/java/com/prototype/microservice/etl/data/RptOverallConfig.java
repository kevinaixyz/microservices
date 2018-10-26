package com.prototype.microservice.etl.data;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class RptOverallConfig {
	List<CommonConfigInfo> configList;

	public List<CommonConfigInfo> getConfigList() {
		return configList;
	}

	public void setConfigList(List<CommonConfigInfo> configList) {
		this.configList = configList;
	}
}
