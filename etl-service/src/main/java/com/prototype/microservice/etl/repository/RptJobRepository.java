package com.prototype.microservice.etl.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.prototype.microservice.etl.domain.RptJobStatus;

@Repository
public class RptJobRepository implements JpaRepository<RptJobStatus, String> {
	
	private EntityManager em;
	
	@Override
	public RptJobStatus findOne(String jobId) {
		String queryStr = "from RptJobStatus c where c.jobId=:jobId";
		TypedQuery<RptJobStatus> query = em.createQuery(queryStr, RptJobStatus.class);
		query.setParameter("jobId", jobId);
		List<RptJobStatus> results = query.setMaxResults(1).getResultList();
		if(results!=null&&results.size()>0){
			return results.get(0);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = false)
	public RptJobStatus save(RptJobStatus rptJobStatus) {
		em.persist(rptJobStatus);
		em.flush();
		return rptJobStatus;
	}
	
	@Transactional(readOnly = false)
	public RptJobStatus update(RptJobStatus rptJobStatus) {
		em.merge(rptJobStatus);
		em.flush();
		return rptJobStatus;
	}

	@Override
	public Page<RptJobStatus> findAll(Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean exists(String id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public long count() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void delete(String id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(RptJobStatus entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Iterable<? extends RptJobStatus> entities) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteAll() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <S extends RptJobStatus> S findOne(Example<S> example) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends RptJobStatus> Page<S> findAll(Example<S> example, Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends RptJobStatus> long count(Example<S> example) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public <S extends RptJobStatus> boolean exists(Example<S> example) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<RptJobStatus> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<RptJobStatus> findAll(Sort sort) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<RptJobStatus> findAll(Iterable<String> ids) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends RptJobStatus> List<S> save(Iterable<S> entities) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void flush() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <S extends RptJobStatus> S saveAndFlush(S entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteInBatch(Iterable<RptJobStatus> entities) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteAllInBatch() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public RptJobStatus getOne(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends RptJobStatus> List<S> findAll(Example<S> example) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends RptJobStatus> List<S> findAll(Example<S> example, Sort sort) {
		// TODO Auto-generated method stub
		return null;
	}

}
