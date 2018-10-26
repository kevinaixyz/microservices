package com.prototype.microservice.etl.cdms.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

@Entity
@Table(name = "RPT_JOB_STATUS")
public class RptJobStatus implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name="JOB_ID")
	String jobId;
	
	@Column(name="STATUS")
	String status;
	
	@Column(name="START_TIME")
	@Temporal(TemporalType.DATE)	
	Date startDatetime;
	
	@Column(name="END_TIME")
	@Temporal(TemporalType.DATE)	
	Date endDatetime;
	
	@Column(name="RES_LIST")
	String resultList;
	
	public String getJobId() {
		return jobId;
	}
	public void setJobId(String jobId) {
		this.jobId = jobId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getStartDatetime() {
		return startDatetime;
	}
	public void setStartDatetime(Date startDatetime) {
		this.startDatetime = startDatetime;
	}
	public Date getEndDatetime() {
		return endDatetime;
	}
	public void setEndDatetime(Date endDatetime) {
		this.endDatetime = endDatetime;
	}
	public String getResultList() {
		return resultList;
	}
	public void setResultList(String resultList) {
		this.resultList = resultList;
	}
	
}
