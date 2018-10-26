package com.prototype.microservice.cms.batch.mapper;

import org.apache.poi.ss.usermodel.Row;

public interface PoiRowMapper<T> {
	T mapRow(Row row);
}
