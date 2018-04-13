package com.junyou.bus.chenghao.configure.export;

import java.util.Map;

import com.kernel.data.dao.AbsVersion;

public class ChengHaoPeiZhiConfig extends AbsVersion {
	/**
	 * 称号id
	 */
	private int id;
	/**
	 * 称号所属类型 1：平台称号 2：成就称号
	 */
	private int type1;
	/**
	 * 头衔名称
	 */
	private String name;
	/**
	 * 资源
	 */
	private String  res;
	
	
	/**
	 * 属性加成
	 */
	private Map<String, Long> attrs;
	/**
	 * 需求等级
	 */
	private int needlevel;
	/**
	 * 需求道具id
	 */
	private String needitem;
	/**
	 * 关联成就id
	 */
	private int id1;
	/**
	 * 称号拥有时间（分）
	 */
	private int time;
	/**
	 * 是否初始激活
	 */
	private int init;
	/**
	 * 平台id
	 */
	private String pingtaiid;

	private Map<String, Long> extraAttrs;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getType1() {
		return type1;
	}

	public void setType1(int type1) {
		this.type1 = type1;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRes() {
		return res;
	}

	public void setRes(String res) {
		this.res = res;
	}

	public Map<String, Long> getAttrs() {
		return attrs;
	}

	public void setAttrs(Map<String, Long> attrs) {
		this.attrs = attrs;
	}

	public int getNeedlevel() {
		return needlevel;
	}

	public void setNeedlevel(int needlevel) {
		this.needlevel = needlevel;
	}

	public String getNeeditem() {
		return needitem;
	}

	public void setNeeditem(String needitem) {
		this.needitem = needitem;
	}

	public int getId1() {
		return id1;
	}

	public void setId1(int id1) {
		this.id1 = id1;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public String getPingtaiid() {
		return pingtaiid;
	}

	public void setPingtaiid(String pingtaiid) {
		this.pingtaiid = pingtaiid;
	}

	public Map<String, Long> getExtraAttrs() {
		return extraAttrs;
	}

	public void setExtraAttrs(Map<String, Long> extraAttrs) {
		this.extraAttrs = extraAttrs;
	}

	public int getInit() {
		return init;
	}

	public void setInit(int init) {
		this.init = init;
	}

}
