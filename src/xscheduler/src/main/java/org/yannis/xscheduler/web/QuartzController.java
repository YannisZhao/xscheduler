/** 
 * Copyright (c) 2016, zhaoyjun0222@gmail.com All Rights Reserved.  
 */

package org.yannis.xscheduler.web;

import java.util.List;
import java.util.Map;

import org.quartz.Job;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.yannis.xscheduler.QuartzManager;

@Controller
@RequestMapping("/scheduler")
public class QuartzController {
	
	@Autowired
	private QuartzManager quartzManager;
	
	@Autowired
	private QuartzService quartzService;

	@RequestMapping("listJobClasses")
	@ResponseBody
	public List<Map<String,Object>> listJobClasses(){
		return quartzManager.getJobClassNames();
	}
	@RequestMapping("list")
	@ResponseBody
	public PageBean list(JobInfo jobInfo, @RequestParam int page, @RequestParam int rows){
		
		PageBean pageBean = quartzService.getList(jobInfo,page,rows);
		List<Map<String, Object>> list = pageBean.getRows();
		int size = list.size();
		for(int i =0; i<size; i++){
			try {
				int state = quartzManager.getJobState((String)list.get(i).get("TRIGGER_NAME"), (String)list.get(i).get("TRIGGER_GROUP"));
				list.get(i).put("STATE", state);
			} catch (SchedulerException e) {
				e.printStackTrace();
			}
		}
		
		return pageBean;
		
	}
	@RequestMapping("add")
	@ResponseBody
	public ResponseMsg add(JobInfo jobInfo){
		ResponseMsg msg = new ResponseMsg();
		try {
			Class<? extends Job> jobClazz = (Class<? extends Job>) Class.forName(jobInfo.getJOB_CLASS_NAME());
			quartzManager.newJob(jobClazz, jobInfo.getJOB_NAME(), jobInfo.getJOB_GROUP_NAME(), jobInfo.getTRIGGER_NAME(), jobInfo.getTRIGGER_GROUP_NAME(), jobInfo.getCRON_EXPRESSION(), jobInfo.getDESCRIPTION());
			msg.setStatus(true);
			msg.setMessage("Operation success");
		} catch (Exception e) {
			e.printStackTrace();
			msg.setStatus(false);
			msg.setMessage(e.getMessage());
		}
		return msg;
		
	}
	@RequestMapping("modifyCron")
	@ResponseBody
	public ResponseMsg modifyCron(JobInfo jobInfo){
		ResponseMsg msg = new ResponseMsg();
		try {
			quartzManager.modifyCron(jobInfo.getTRIGGER_NAME(), jobInfo.getTRIGGER_GROUP_NAME(), jobInfo.getCRON_EXPRESSION());
			msg.setStatus(true);
			msg.setMessage("Operation success");
		} catch (SchedulerException e) {
			e.printStackTrace();
			msg.setStatus(false);
			msg.setMessage(e.getMessage());
		}
		return msg;
	}
	@RequestMapping("remove")
	@ResponseBody
	public ResponseMsg remove(JobInfo jobInfo){
		ResponseMsg msg = new ResponseMsg();
		try {
			quartzManager.removeJob(jobInfo.getJOB_NAME(), jobInfo.getJOB_GROUP_NAME(), jobInfo.getTRIGGER_NAME(), jobInfo.getTRIGGER_GROUP_NAME());
			if(quartzService.removeJob(jobInfo)){
				msg.setStatus(true);
				msg.setMessage("Operation success");
			}else{
				msg.setStatus(false);
				msg.setMessage("Operation error in db");
			}
		} catch (Exception e) {
			e.printStackTrace();
			msg.setStatus(false);
			msg.setMessage(e.getMessage());
		}
		return msg;
		
	}
	@RequestMapping("start_job")
	@ResponseBody
	public ResponseMsg startJob(JobInfo jobInfo){
		ResponseMsg msg = new ResponseMsg();
		try {
			quartzManager.resumeJob(jobInfo.getJOB_NAME(), jobInfo.getJOB_GROUP_NAME(), jobInfo.getTRIGGER_NAME(), jobInfo.getTRIGGER_GROUP_NAME());
			msg.setStatus(true);
			msg.setMessage("Operation success");
		} catch (Exception e) {
			e.printStackTrace();
			msg.setStatus(false);
			msg.setMessage(e.getMessage());
		}
		return msg;
		
	}
	@RequestMapping("stop_job")
	@ResponseBody
	public ResponseMsg stopJob(JobInfo jobInfo){
		ResponseMsg msg = new ResponseMsg();
		try {
			quartzManager.pauseJob(jobInfo.getJOB_NAME(), jobInfo.getJOB_GROUP_NAME());
			msg.setStatus(true);
			msg.setMessage("Operation success");
		} catch (Exception e) {
			e.printStackTrace();
			msg.setStatus(false);
			msg.setMessage(e.getMessage());
		}
		return msg;
	}
	@RequestMapping("start_scheduler")
	@ResponseBody
	public ResponseMsg startScheduler(){
		ResponseMsg msg = new ResponseMsg();
		try {
			quartzManager.startScheduler();
			msg.setStatus(true);
			msg.setMessage("Operation success");
		} catch (Exception e) {
			e.printStackTrace();
			msg.setStatus(false);
			msg.setMessage(e.getMessage());
		}
		return msg;
		
	}
	@RequestMapping("stop_scheduler")
	@ResponseBody
	public ResponseMsg stopScheduler(){
		ResponseMsg msg = new ResponseMsg();
		try {
			quartzManager.stopScheduler();
			msg.setStatus(true);
			msg.setMessage("Operation success");
		} catch (Exception e) {
			e.printStackTrace();
			msg.setStatus(false);
			msg.setMessage(e.getMessage());
		}
		return msg;
		
	}
	
}
