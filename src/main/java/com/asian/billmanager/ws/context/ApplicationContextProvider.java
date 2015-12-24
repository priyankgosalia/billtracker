package com.asian.billmanager.ws.context;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/*
 * ApplicationContextProvider
 * 
 * Created: 22-DEC-2015
 * Author:  Priyank Gosalia <priyankmg@gmail.com>
 */

public final class ApplicationContextProvider implements ApplicationContextAware {

	private static ApplicationContext ctx = null;
	
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.ctx = applicationContext;
	}

	public static ApplicationContext getCtx() {
		return ctx;
	}
}
