package com.hehj.easyexecutor.config;

public class InterceptorConfig {

	private String className;
	
	private Boolean springBean;

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public Boolean isSpringBean() {
		return springBean;
	}

	public void setSpringBean(String springBean) {
		this.springBean = Boolean.valueOf(springBean);
	}
	
	
}
