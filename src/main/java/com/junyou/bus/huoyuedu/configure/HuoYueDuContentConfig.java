package com.junyou.bus.huoyuedu.configure;

/**
 * 活跃度内容vo
 * @author lxn
 *
 */
public class HuoYueDuContentConfig{

	private Integer id; //具体活动id
	private Integer needNum; //活动需要完成的总次数
	private Integer jifen; //完成奖励积分
	private Integer data;  //限定条件
	
	public Integer getData() {
		return data;
	}
	
	public void setData(Integer data) {
		this.data = data;
	}
	 
	 public Integer getId() {
		return id;
	}
	 public void setId(Integer id) {
		this.id = id;
	}
	public Integer getNeedNum() {
		return needNum;
	}
	
	public void setNeedNum(Integer needNum) {
		this.needNum = needNum;
	}
	public Integer getJifen() {
		return jifen;
	}
	
	public void setJifen(Integer jifen) {
		this.jifen = jifen;
	}
}
