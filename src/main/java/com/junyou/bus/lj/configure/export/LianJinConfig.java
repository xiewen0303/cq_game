package com.junyou.bus.lj.configure.export;

import com.kernel.data.dao.AbsVersion;

public class LianJinConfig extends AbsVersion {


	private int id;
	private int needtype;
	private int num;
	private int needcount;
	private int exp;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getNeedtype() {
		return needtype;
	}
	public void setNeedtype(int needtype) {
		this.needtype = needtype;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public int getNeedcount() {
		return needcount;
	}
	public void setNeedcount(int needcount) {
		this.needcount = needcount;
	}
	public int getExp() {
		return exp;
	}
	public void setExp(int exp) {
		this.exp = exp;
	}
}
