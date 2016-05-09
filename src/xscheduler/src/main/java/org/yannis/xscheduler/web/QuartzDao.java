/** 
 * Copyright (c) 2016, zhaoyjun0222@gmail.com All Rights Reserved.  
 */   
      
package org.yannis.xscheduler.web;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class QuartzDao {
	
	private static final String DELETE_JOB = "delete from QRTZ_JOB_DETAILS where SCHED_NAME=? and JOB_NAME=? and JOB_GROUP=?";
	private static final String DELETE_TRIGGER = "delete from QRTZ_TRIGGERS where SCHED_NAME=? and JOB_NAME=? and JOB_GROUP=?";
	private static final String DELETE_CRON = "delete from QRTZ_CRON_TRIGGERS where SCHED_NAME=? and TRIGGER_NAME=? and TRIGGER_GROUP=?";
	private static final String QUERY_JOB_LIST = "SELECT jobs.SCHED_NAME,jobs.JOB_NAME,jobs.JOB_GROUP,jobs.DESCRIPTION,jobs.JOB_CLASS_NAME,jobs.IS_DURABLE,jobs.IS_NONCONCURRENT,jobs.IS_UPDATE_DATA,jobs.REQUESTS_RECOVERY,jobs.JOB_DATA,tgrs.TRIGGER_NAME,tgrs.TRIGGER_GROUP,tgrs.PREV_FIRE_TIME,tgrs.NEXT_FIRE_TIME,tgrs.PRIORITY,tgrs.TRIGGER_STATE,tgrs.START_TIME,tgrs.END_TIME,tgrs.CALENDAR_NAME,tgrs.MISFIRE_INSTR,tgrs.CALENDAR_NAME,crons.CRON_EXPRESSION FROM QRTZ_JOB_DETAILS jobs JOIN QRTZ_TRIGGERS tgrs ON jobs.SCHED_NAME = tgrs.SCHED_NAME AND jobs.JOB_NAME=tgrs.JOB_NAME AND jobs.JOB_GROUP=tgrs.JOB_GROUP JOIN QRTZ_CRON_TRIGGERS crons ON tgrs.SCHED_NAME=crons.SCHED_NAME AND tgrs.TRIGGER_NAME=crons.TRIGGER_NAME AND tgrs.TRIGGER_GROUP=crons.TRIGGER_GROUP";
	private static final String QUERY_JOB_COUNT = "SELECT count(1) FROM QRTZ_JOB_DETAILS jobs JOIN QRTZ_TRIGGERS tgrs ON jobs.SCHED_NAME = tgrs.SCHED_NAME AND jobs.JOB_NAME=tgrs.JOB_NAME AND jobs.JOB_GROUP=tgrs.JOB_GROUP JOIN QRTZ_CRON_TRIGGERS crons ON tgrs.SCHED_NAME=crons.SCHED_NAME AND tgrs.TRIGGER_NAME=crons.TRIGGER_NAME AND tgrs.TRIGGER_GROUP=crons.TRIGGER_GROUP";
	
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public long getTotal(JobInfo jobInfo) {
		return jdbcTemplate.queryForObject(QUERY_JOB_COUNT, Long.class);
	}

	public List<Map<String, Object>> getList(JobInfo jobInfo, int start, int count) {
		String sql = QUERY_JOB_LIST + " limit ?,?";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql,new Object[]{start,count});
		return list;
	}

	public boolean removeJob(JobInfo jobInfo) {
		jdbcTemplate.update(DELETE_CRON,new Object[]{jobInfo.getSCHED_NAME(),jobInfo.getTRIGGER_NAME(),jobInfo.getTRIGGER_GROUP_NAME()});
		jdbcTemplate.update(DELETE_TRIGGER,new Object[]{jobInfo.getSCHED_NAME(),jobInfo.getJOB_NAME(),jobInfo.getJOB_GROUP_NAME()});
		jdbcTemplate.update(DELETE_JOB,new Object[]{jobInfo.getSCHED_NAME(),jobInfo.getJOB_NAME(),jobInfo.getJOB_GROUP_NAME()});
		return true;
	}

}
  