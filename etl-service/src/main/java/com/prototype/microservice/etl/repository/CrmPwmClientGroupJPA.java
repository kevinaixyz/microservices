package com.prototype.microservice.etl.repository;

import java.util.List;

public interface CrmPwmClientGroupJPA {

	public List<CrmPwmClientGroup> searchClientGroup(CrmPwmClientGroupRequestJson criterialDTO);
}
