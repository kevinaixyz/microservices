package com.prototype.microservice.cms.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class BaseRepository {
    @Autowired
    protected EntityManager em;
}