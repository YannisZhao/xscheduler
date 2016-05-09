/** 
 * Copyright (c) 2016, zhaoyjun0222@gmail.com All Rights Reserved.  
 */   
      
package org.yannis.xscheduler.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuartzService {
	
	@Autowired
	private QuartzDao quartzDao;

	public PageBean getList(JobInfo jobInfo, int page, int rows) {
		PageBean pageBean = new PageBean();
		pageBean.setTotal(quartzDao.getTotal(jobInfo));
		pageBean.setRows(quartzDao.getList(jobInfo,(page-1)*rows,rows));
		return pageBean;
	}

	public boolean removeJob(JobInfo jobInfo) {
		return quartzDao.removeJob(jobInfo);
	}

}
  