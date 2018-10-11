package com.prototype.microservice.commons.json;

import java.util.List;

@Deprecated
public class ResponseJsonWrapper<T> extends SimpleResponseJson {

	private static final long serialVersionUID = 2558116835141935978L;
	private List<T> dtoList;

	public ResponseJsonWrapper(final String instanceId) {
		super(instanceId);
	}

	public List<T> getDTOList() {
		return dtoList;
	}

	public void setDTOList(final List<T> dtoList) {
		this.dtoList = dtoList;
	}

}
