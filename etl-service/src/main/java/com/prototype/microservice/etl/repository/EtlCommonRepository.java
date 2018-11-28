package com.prototype.microservice.etl.repository;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class EtlCommonRepository {
	@Autowired
	@PersistenceContext
	protected EntityManager entityManager;
	
	@Transactional(readOnly = true)
	public BigDecimal execCount(String sql){
		//System.out.println("===>"+sql);
		Query query = entityManager.createNativeQuery(sql);
		Object res = query.getSingleResult();
		BigDecimal recNo = new BigDecimal(String.valueOf(res));
		return recNo;
	}
	
	@Transactional(readOnly = false)
	public int execUpdate(String sql){
		//System.out.println("===>"+sql);
		Query query = entityManager.createNativeQuery(sql);
		int num = query.executeUpdate();
		//entityManager.flush();
		return num;
	}
	
	@Transactional(readOnly = false)
	public int insertDataByParams(String sql, List<Object> values){
		//System.out.println("===>"+sql);
		Query query = entityManager.createQuery(sql);
		for(int i=0;i<values.size();i++){
			query.setParameter(i, values.get(i));
		}
		return query.executeUpdate();
	}
}
