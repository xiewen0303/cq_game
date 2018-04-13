package com.junyou.bus.daytask.configure;


import java.util.HashMap;
import java.util.Map;

import com.kernel.data.dao.AbsVersion;

/**
 * 
 * @description 任务总环数 
 *
 * @author wind
 * @date 2015-03-16 16:09:39
 */
public class RiChangJiangLiConfig extends AbsVersion {

	private Integer id;

	private Integer maxcount;

	private Integer minlevel;
	
	private Integer maxlevel;
	
	private Integer type;
	
	private Map<String,Integer> awards = new HashMap<String, Integer>();
	
	private Integer jiangmoney;
	
	private Integer jiangexp;
	
	private Integer jiangzhen;
	
	private Integer jianggongxian;
	
	public Integer getJiangmoney() {
		return jiangmoney;
	}

	public void setJiangmoney(Integer jiangmoney) {
		this.jiangmoney = jiangmoney;
	}

	public Integer getJiangexp() {
		return jiangexp;
	}

	public void setJiangexp(Integer jiangexp) {
		this.jiangexp = jiangexp;
	}

	public Integer getJiangzhen() {
		return jiangzhen;
	}

	public void setJiangzhen(Integer jiangzhen) {
		this.jiangzhen = jiangzhen;
	}

	public Integer getJianggongxian() {
		return jianggongxian;
	}

	public void setJianggongxian(Integer jianggongxian) {
		this.jianggongxian = jianggongxian;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getMaxcount() {
		return maxcount;
	}

	public void setMaxcount(Integer maxcount) {
		this.maxcount = maxcount;
	}
	public Integer getMaxlevel() {
		return maxlevel;
	}

	public void setMaxlevel(Integer maxlevel) {
		this.maxlevel = maxlevel;
	}
	
 
	public Map<String, Integer> getAwards() {
		return awards;
	}

	public void setAwards(Map<String, Integer> awards) {
		this.awards = awards;
	}

	public Integer getMinlevel() {
		return minlevel;
	}

	public void setMinlevel(Integer minlevel) {
		this.minlevel = minlevel;
	}
	
	public RiChangJiangLiConfig copy(){
		return null;
	}

}
