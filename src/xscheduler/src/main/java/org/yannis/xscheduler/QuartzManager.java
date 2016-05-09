/** 
 * Copyright (c) 2016, zhaoyjun0222@gmail.com All Rights Reserved.  
 */

package org.yannis.xscheduler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.quartz.CronScheduleBuilder;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.Trigger.TriggerState;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.yannis.xscheduler.util.ClassUtils;

public class QuartzManager {

	private Scheduler scheduler;

	private List<String> jobPackages;
	
	private QuartzManager(){}
	
	private static class QuartzManagerSingletonHolder {
		static QuartzManager instance = new QuartzManager();
	}
	
	public static QuartzManager getInstance() {
		return QuartzManagerSingletonHolder.instance;
	}
	
	/**
	 * STATE_BLOCKED 4 STATE_COMPLETE 2 STATE_ERROR 3 STATE_NONE -1 STATE_NORMAL
	 * 0 STATE_PAUSED 1
	 * 
	 * @throws SchedulerException
	 */
	public int getJobState(String triggerName, String triggerGroupName) throws SchedulerException {
		if(triggerGroupName==null || "".equals(triggerGroupName)){
			triggerGroupName = Scheduler.DEFAULT_GROUP;
		}
		TriggerState state = scheduler.getTriggerState(new TriggerKey(triggerName, triggerGroupName));
		return state.ordinal();
	}

	/**
	 * add and start a new job
	 */
	public void newJob(Class<? extends Job> jobClazz, String jobName, String jobGroupName, String triggerName,
			String triggerGroupName, String cronPattern, String description) throws Exception {
		
		if(jobGroupName==null || "".equals(jobGroupName)){
			jobGroupName = Scheduler.DEFAULT_GROUP;
		}
		
		if(triggerGroupName==null || "".equals(triggerGroupName)){
			triggerGroupName = Scheduler.DEFAULT_GROUP;
		}
		
		JobDetail jobDetail = JobBuilder.newJob(jobClazz).withIdentity(jobName, jobGroupName).withDescription(description).build();
		Trigger trigger = TriggerBuilder.newTrigger().withIdentity(triggerName, triggerGroupName)
				.withSchedule(CronScheduleBuilder.cronSchedule(cronPattern)).build();
		scheduler.scheduleJob(jobDetail, trigger);
	}

	/**
	 * save a job
	 */
	public void addJob(Class<? extends Job> jobClazz, String jobName, String jobGroupName) throws Exception {
		
		if(jobGroupName==null || "".equals(jobGroupName)){
			jobGroupName = Scheduler.DEFAULT_GROUP;
		}
		
		JobDetail jobDetail = JobBuilder.newJob(jobClazz).withIdentity(jobName, jobGroupName).build();
		scheduler.addJob(jobDetail, true);
	}

	/**
	 * resume the paused job
	 */
	public void resumeJob(String jobName, String jobGroupName, String triggerName, String triggerGroupName)
			throws SchedulerException {
		
		if(jobGroupName==null || "".equals(jobGroupName)){
			jobGroupName = Scheduler.DEFAULT_GROUP;
		}
		
		if(triggerGroupName==null || "".equals(triggerGroupName)){
			triggerGroupName = Scheduler.DEFAULT_GROUP;
		}
		
		scheduler.resumeJob(new JobKey(jobName, jobGroupName));
		// uaasScheduler.resumeTrigger(new TriggerKey(triggerName,
		// Scheduler.DEFAULT_GROUP));
	}

	/**
	 * pause job
	 */
	public void pauseJob(String jobName, String jobGroupName) throws SchedulerException {
		
		if(jobGroupName==null || "".equals(jobGroupName)){
			jobGroupName = Scheduler.DEFAULT_GROUP;
		}
		
		scheduler.pauseJob(new JobKey(jobName, jobGroupName));
	}

	/**
	 * remove job
	 */
	public void removeJob(String jobName, String triggerName) throws SchedulerException {
		removeJob(jobName, Scheduler.DEFAULT_GROUP, triggerName, Scheduler.DEFAULT_GROUP);
	}

	/**
	 * remove job
	 */
	public void removeJob(String jobName, String jobGroupName, String triggerName, String triggerGroupName)
			throws SchedulerException {
		
		if(jobGroupName==null || "".equals(jobGroupName)){
			jobGroupName = Scheduler.DEFAULT_GROUP;
		}
		
		if(triggerGroupName==null || "".equals(triggerGroupName)){
			triggerGroupName = Scheduler.DEFAULT_GROUP;
		}
		
		TriggerKey triggerKey = new TriggerKey(triggerName, triggerGroupName);
		scheduler.pauseTrigger(triggerKey);
		scheduler.unscheduleJob(triggerKey);
		JobKey jobKey = new JobKey(jobName, jobGroupName);
		scheduler.deleteJob(jobKey);
	}

	public void clear() {
	}

	/**
	 * modify the job
	 */
	public void modifyJob(Class<? extends Job> jobClazz, String jobName, String jobGroupName)
			throws SchedulerException {
		
		if(jobGroupName==null || "".equals(jobGroupName)){
			jobGroupName = Scheduler.DEFAULT_GROUP;
		}
		
		JobDetail jobDetail = JobBuilder.newJob(jobClazz).withIdentity(jobName, jobGroupName).build();
		scheduler.addJob(jobDetail, true);
	}

	/**
	 * modify job trigger cron expression
	 */
	public void modifyCron(String triggerName, String triggerGroupName, String cronPattern) throws SchedulerException {
		
		if(triggerGroupName==null || "".equals(triggerGroupName)){
			triggerGroupName = Scheduler.DEFAULT_GROUP;
		}
		
		Trigger oldTrigger = scheduler.getTrigger(new TriggerKey(triggerName, triggerGroupName));
		Trigger newTrigger = TriggerBuilder.newTrigger().withIdentity(triggerName, triggerGroupName)
				.withSchedule(CronScheduleBuilder.cronSchedule(cronPattern)).build();

		scheduler.rescheduleJob(oldTrigger.getKey(), newTrigger);
	}

	/**
	 * start scheduler
	 */
	public void startScheduler() throws SchedulerException {
		if (scheduler.isShutdown())
			scheduler.start();
	}

	/**
	 * stop scheduler
	 */
	public void stopScheduler() throws SchedulerException {
		if (scheduler.isStarted())
			scheduler.shutdown();
	}

	/**
	 * get valid job class' name
	 */
	public List<Map<String, Object>> getJobClassNames() {
		List<String> classNames = new ArrayList<String>();
		try {
			for(String pkgName : this.jobPackages){
				classNames.addAll(ClassUtils.getClassNamesWithInterface(pkgName, Job.class));
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		List<Map<String,Object>> responseVO = new ArrayList<Map<String,Object>>();
		for(String className : classNames){
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("text", className);
			map.put("value", className);
			responseVO.add(map);
		}
		return responseVO;
	}

	public Scheduler getScheduler() {
		return scheduler;
	}

	public void setScheduler(Scheduler scheduler) {
		this.scheduler = scheduler;
	}

	public void setJobPackages(List<String> jobPackages) {
		this.jobPackages = jobPackages;
	}

}
