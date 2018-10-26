package com.prototype.microservice.aeprotal.repository.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import com.prototype.microservice.aeprotal.AbstractCrmPwmServiceTest;
import com.prototype.microservice.ficc.domain.CrmPwmClientGroup;
import com.prototype.microservice.ficc.json.CrmPwmClientGroupRequestJson;
import com.prototype.microservice.ficc.repository.CrmPwmClientGroupRepository;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CrmPwmClientGroupRepositoryImplTest extends AbstractCrmPwmServiceTest {

	@Autowired
	private CrmPwmClientGroupRepository repo;

	@Test
	public void test01_searchClientGroup_withResult() {

		CrmPwmClientGroupRequestJson criteria = new CrmPwmClientGroupRequestJson();

		List<CrmPwmClientGroup> result = repo.searchClientGroup(criteria);
		Assert.assertTrue(!result.isEmpty());

	}

	@Test
	public void test02_searchClientGroup_withCcisClientGrpId() {

		CrmPwmClientGroupRequestJson criteria = new CrmPwmClientGroupRequestJson();
		criteria.setCcisClientGroupID("1");

		List<CrmPwmClientGroup> result = repo.searchClientGroup(criteria);
		Assert.assertTrue(result.size() == 2);

	}

	@Test
	public void test03_searchClientGroup_withRelationshipName() {

		CrmPwmClientGroupRequestJson criteria = new CrmPwmClientGroupRequestJson();
		criteria.setRelationshipName("Shareholder");

		List<CrmPwmClientGroup> result = repo.searchClientGroup(criteria);
		Assert.assertTrue(result.size() == 1);

	}

	@Test
	public void test03_searchClientGroup_withExpiryDateFrom() {

		CrmPwmClientGroupRequestJson criteria = new CrmPwmClientGroupRequestJson();
		String dateString = "2017-01-06 ";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd ");
		Date date = new Date();
		try {
			date = sdf.parse(dateString);
		}
		catch (ParseException e) {
			e.printStackTrace();
		}
		DateTime dateTime = new DateTime(date);
		criteria.setExpiryDateFrom(ISODateTimeFormat.date().print(dateTime));

		List<CrmPwmClientGroup> result = repo.searchClientGroup(criteria);
		Assert.assertTrue(result.size() == 2);

	}

	@Test
	public void test04_searchClientGroup_withExpiryDateTo() {

		CrmPwmClientGroupRequestJson criteria = new CrmPwmClientGroupRequestJson();
		String dateString = "2018-01-06 ";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd ");
		Date date = new Date();
		try {
			date = sdf.parse(dateString);
		}
		catch (ParseException e) {
			e.printStackTrace();
		}
		DateTime dateTime = new DateTime(date);
		criteria.setExpiryDateTo(ISODateTimeFormat.date().print(dateTime));

		List<CrmPwmClientGroup> result = repo.searchClientGroup(criteria);
		Assert.assertTrue(result.size() == 2);

	}

}
