package com.junyou.stage.model.stage.tanbao;
/**
 * 探宝人物VO
 * @author LiuYu
 * 2015-6-16 下午4:33:02
 */
public class TanBaoRoleVo {
	private long userRoleId;
	private String name;
	private int deadTime;
	private int score;
	private long exp;
	private long enterTime;
	private int rank;
	private Object[] msgData;
	public long getUserRoleId() {
		return userRoleId;
	}
	public void setUserRoleId(long userRoleId) {
		this.userRoleId = userRoleId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getDeadTime() {
		return deadTime;
	}
	public void setDeadTime(int deadTime) {
		this.deadTime = deadTime;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public long getExp() {
		return exp;
	}
	public void setExp(long exp) {
		this.exp = exp;
	}
	public long getEnterTime() {
		return enterTime;
	}
	public void setEnterTime(long enterTime) {
		this.enterTime = enterTime;
	}
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
	
	public Object[] getMsgData(){
		if(msgData == null){
			msgData = new Object[]{
				name
				,userRoleId
				,rank
				,score
				,deadTime
			};
		}else{
			msgData[2] = rank;
			msgData[3] = score;
			msgData[4] = deadTime;
		}
		return msgData;
	}
	
}
