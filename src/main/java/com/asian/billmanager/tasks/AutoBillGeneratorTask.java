package com.asian.billmanager.tasks;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/*
 * AutoBillGeneratorTask
 * 
 * An automated, scheduled Quartz task that runs 
 * periodically everyday to check if new bills need
 * to be auto-generated depending on their type and
 * due date. (only for recurring bills).
 * 
 * Created: 02-JAN-2015
 * Author:  Priyank Gosalia <priyankmg@gmail.com>
 */
public class AutoBillGeneratorTask {
	private static final Logger logger = LogManager.getLogger(AutoBillGeneratorTask.class.getName());
	
	public void run() {
		logger.info("*** Automated Bill Generator Task - start ***");
		logger.info("*** Automated Bill Generator Task - end ***");
	}
}
