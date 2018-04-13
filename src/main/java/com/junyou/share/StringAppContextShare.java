package com.junyou.share;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 */
public class StringAppContextShare {

	private static ApplicationContext springContext;
	
	
	public static void init(){
		initSpringContext();
	}
	
	public static ApplicationContext getSpringContext() {
		return springContext;
	}
	
	private static void initSpringContext(){
		springContext = new ClassPathXmlApplicationContext("config/spring/applicationContext.xml");
	}
	
}
