package com.kernel.check.db;

/**
 * jdbc 配置信息bean
 * @author DaoZheng Yuan
 * 2014年12月22日 下午6:37:10
 */
public class JdbcCheckBean {
	
	private String jdbcUrl;
	
	private String user;
	
	private String password;

	public String getJdbcUrl() {
		return jdbcUrl;
	}

	public void setJdbcUrl(String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
