package com.junyou.bus.daytask.configure;

import java.util.HashMap;
import java.util.Map;
import com.kernel.data.dao.AbsVersion;

/**
 * 
 * @description 日常任务表 
 *
 * @author wind
 * @date 2015-03-16 15:11:33
 */
public class RiChangRenWuConfig extends AbsVersion {

	private Integer id;
	
	private Integer type;

	private Integer minlevel;

	private Integer maxlevel;
	
	private String monster;
	
	private Integer map;
	
	private Integer num;
	
	private Integer jiangmoney;
	
	private Integer jiangexp;
	
	private Integer jiangzhen;
	
	private Integer jianggongxian;

	private Map<String,Integer> awards = new HashMap<String, Integer>();
 
	private Integer finish; 

	private boolean fly;
	
	private String zuoBiao;

	public String getZuoBiao() {
		return zuoBiao;
	}

	public void setZuoBiao(String zuoBiao) {
		this.zuoBiao = zuoBiao;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getMinlevel() {
		return minlevel;
	}

	public void setMinlevel(Integer minlevel) {
		this.minlevel = minlevel;
	}

	public Integer getMaxlevel() {
		return maxlevel;
	}

	public void setMaxlevel(Integer maxlevel) {
		this.maxlevel = maxlevel;
	} 
	
	public String getMonster() {
		return monster;
	}

	public void setMonster(String monster) {
		this.monster = monster;
	}

	public Integer getMap() {
		return map;
	}

	public void setMap(Integer map) {
		this.map = map;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

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

	public Map<String, Integer> getAwards() {
		return awards;
	}

	public void setAwards(Map<String, Integer> awards) {
		this.awards = awards;
	}

	public Integer getFinish() {
		return finish;
	}

	public void setFinish(Integer finish) {
		this.finish = finish;
	}

	public boolean isFly() {
		return fly;
	}

	public void setFly(boolean fly) {
		this.fly = fly;
	}
}
