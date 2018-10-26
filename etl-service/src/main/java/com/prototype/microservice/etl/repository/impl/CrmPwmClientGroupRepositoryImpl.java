package com.prototype.microservice.etl.repository.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.joda.time.format.ISODateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.prototype.microservice.etl.repository.CrmPwmClientGroupJPA;


/**
 * Repository implementation for project module
 *
 */
public class CrmPwmClientGroupRepositoryImpl implements CrmPwmClientGroupJPA {

	@PersistenceContext(unitName = "crmpwm")
	private EntityManager em;


	private static final Logger LOG = LoggerFactory.getLogger(CrmPwmClientGroupRepositoryImpl.class);
	SimpleDateFormat sdFormat=new SimpleDateFormat("yyyy/MMdd HH:mm:ss");

	@SuppressWarnings("unchecked")
	@Override
	public List<CrmPwmClientGroup> searchClientGroup(CrmPwmClientGroupRequestJson criterialDTO){
		final StringBuilder queryStr = new StringBuilder(" from CrmPwmClientGroup c where 1=1");
		if (StringUtils.isNotBlank(criterialDTO.getClientGroupMemberRid())) {
			queryStr.append(" AND 	c.clientGroupMemberRid = :clientGroupMemberRid ");
		}
		if (StringUtils.isNotBlank(criterialDTO.getCcisClientGroupID())) {
			queryStr.append(" AND 	c.ccisClientGroupID = :ccisClientGroupID ");
		}
		if (StringUtils.isNotBlank(criterialDTO.getClientGroupName())) {
			queryStr.append(" AND 	(LOWER(c.clientGroupName) like  :clientGroupName)");
		}

		if (StringUtils.isNotBlank(criterialDTO.getCcisClientID())) {
			queryStr.append(" AND (LOWER(c.ccisClientID) like :ccisClientID )");
		}
		if (StringUtils.isNotBlank(criterialDTO.getClientName())) {
			queryStr.append(" AND (LOWER(c.clientName) like  :clientName)");
		}
		if (StringUtils.isNotBlank(criterialDTO.getPercentageOwnership())) {
			queryStr.append(" AND c.percentageOwnership= :percentageOwnership");
		}
		if (StringUtils.isNotBlank(criterialDTO.getRelatedTo())) {
			queryStr.append(" AND 	(LOWER(c.relatedTo) like  :relatedTo) ");
		}
		if (StringUtils.isNotBlank(criterialDTO.getRelationshipName())) {
			queryStr.append(" AND 	(LOWER(c.relationshipName) like  :relationshipName) ");
		}
		if (StringUtils.isNotBlank(criterialDTO.getExpiryDateFrom())) {
			queryStr.append(" AND 	c.expiryDate >= :dateFrom ");

		}
		if (StringUtils.isNotBlank(criterialDTO.getExpiryDateTo())) {
			queryStr.append(" AND 	c.expiryDate <= :dateTo ");
		}

		final Query query = em.createQuery(queryStr.toString());
		if (StringUtils.isNotBlank(criterialDTO.getClientGroupMemberRid())) {
			query.setParameter("clientGroupMemberRid", Long.valueOf(criterialDTO.getClientGroupMemberRid()));
		}
		if (StringUtils.isNotBlank(criterialDTO.getCcisClientGroupID())) {
			query.setParameter("ccisClientGroupID", Long.valueOf(criterialDTO.getCcisClientGroupID()));
		}

		if (StringUtils.isNotBlank(criterialDTO.getClientGroupName())) {
			query.setParameter("clientGroupName", "%" +criterialDTO.getClientGroupName().toLowerCase()+"%");
		}
		if (StringUtils.isNotBlank(criterialDTO.getCcisClientID())) {
			query.setParameter("ccisClientID", "%" +criterialDTO.getCcisClientID()+"%");
		}
		if (StringUtils.isNotBlank(criterialDTO.getClientName())) {
			query.setParameter("clientName", "%" +criterialDTO.getClientName()+"%");
		}
		if (StringUtils.isNotBlank(criterialDTO.getPercentageOwnership())) {
			query.setParameter("percentageOwnership", new BigDecimal(criterialDTO.getPercentageOwnership()));
		}
		if (StringUtils.isNotBlank(criterialDTO.getRelatedTo())) {
			query.setParameter("relatedTo","%" +criterialDTO.getRelatedTo().toLowerCase()+"%");
		}
		if (StringUtils.isNotBlank(criterialDTO.getRelationshipName())) {
			query.setParameter("relationshipName","%" +criterialDTO.getRelationshipName().toLowerCase()+"%");
		}

		if (StringUtils.isNotBlank(criterialDTO.getExpiryDateFrom())) {

			Date dateFrom=ISODateTimeFormat.dateTimeParser().parseDateTime(criterialDTO.getExpiryDateFrom()).toDate();
			query.setParameter("dateFrom",dateFrom);
		}
		if (StringUtils.isNotBlank(criterialDTO.getExpiryDateTo())) {

	     	Date dateTo=ISODateTimeFormat.dateTimeParser().parseDateTime(criterialDTO.getExpiryDateTo()).toDate();
	     	try {
				dateTo=sdFormat.parse(sdFormat.format(dateTo));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(dateTo);
			calendar.add(Calendar.DATE, 1);
			calendar.add(Calendar.SECOND,-1);
			query.setParameter("dateTo",calendar.getTime());
		}



		return query.getResultList();
	}


}
