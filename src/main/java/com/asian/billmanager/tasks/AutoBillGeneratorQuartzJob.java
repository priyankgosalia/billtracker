package com.asian.billmanager.tasks;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

/*
 * AutoBillGeneratorQuartzJob
 * 
 * Created: 02-JAN-2015
 * Author:  Priyank Gosalia <priyankmg@gmail.com>
 */
public class AutoBillGeneratorQuartzJob extends QuartzJobBean {
	private static final Logger logger = LogManager.getLogger(AutoBillGeneratorQuartzJob.class.getName());
	private AutoBillGeneratorTask autoBillGenTask;
 
	public void setAutoBillGenTask(AutoBillGeneratorTask aTask) {
		this.autoBillGenTask = aTask;
	}
	 
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		this.autoBillGenTask.run();
	}
} 