package com.junyou.gameconfig.publicconfig.configure.export;

/**
 * 新圣剑
 */
public class MailPublicConfig extends AdapterPublicConfig{
	
	private int maxnum;//邮件最大数
	private long gouqi1; //过期时间
	private long gouqi2; //过期时间

	public int getMaxnum() {
		return maxnum;
	}

	public void setMaxnum(int maxnum) {
		this.maxnum = maxnum;
	}

	public long getGouqi1() {
		return gouqi1;
	}

	public void setGouqi1(long gouqi1) {
		this.gouqi1 = gouqi1;
	}

	public long getGouqi2() {
		return gouqi2;
	}

	public void setGouqi2(long gouqi2) {
		this.gouqi2 = gouqi2;
	}
 
}
