package com.prototype.microservice.etl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CrmPwmClientGroupRepository extends JpaRepository<CrmPwmClientGroup, Long>, CrmPwmClientGroupJPA {

}
