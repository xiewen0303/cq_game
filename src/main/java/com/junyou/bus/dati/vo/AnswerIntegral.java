package com.junyou.bus.dati.vo;

import java.util.LinkedHashSet;
import java.util.Set;


/** 玩家答题积分 */
public class AnswerIntegral implements Comparable<AnswerIntegral> {
	private long userRoleId;

	private int rank;

	private String name;

	private int integral;
	
	private int doublePointCard;
	
	private int excludeCard;
	
	private int level;
	
	/**玩家答题记录，每题每人只能答一次	 
	 * Integer:题目ID 
	 **/
	private Set<Integer> answereds; 
	
	public AnswerIntegral(long userRoleId,String name,int doublePointCard,int excludeCard,int defaultRank,int level){
		this.userRoleId=userRoleId;
		this.name=name;
		this.doublePointCard=doublePointCard;
		this.excludeCard=excludeCard;
		this.rank=defaultRank;
		this.level=level;
		answereds=new LinkedHashSet<>();
		
	}
	
	public long getUserRoleId() {
		return userRoleId;
	}

	public void setUserRoleId(long userRoleId) {
		this.userRoleId = userRoleId;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getIntegral() {
		return integral;
	}

	public void setIntegral(int integral) {
		this.integral = integral;
	}

	public void addIntegral(int integral){
		this.integral=this.integral+integral;
	}
	
	public int getDoublePointCard() {
		return doublePointCard;
	}
	public boolean isHaveDoublePointCard(){
		return doublePointCard>0;
	}
	
	
	public void setDoublePointCard(int doublePointCard) {
		this.doublePointCard = doublePointCard;
	}

	public int getExcludeCard() {
		return excludeCard;
	}
	public boolean isHaveExcludeCard(){
		return excludeCard>0;
	}

	public void setExcludeCard(int excludeCard) {
		this.excludeCard = excludeCard;
	}
	
	public void useExcludeCard(){
		if(isHaveExcludeCard()){
			--this.excludeCard;
		}
	}

	public void useDoublePointCard(){
		if(isHaveDoublePointCard()){
			--this.doublePointCard;
		}
	}
	
	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
	/**是否答过此题*/
	public boolean isAnswered(int timuId){
		return this.answereds.contains(timuId);
	}
	/**添加答题记录*/
	public boolean addAnswered(int timuId){
		if(isAnswered(timuId)){
			return false;
		}
		return answereds.add(timuId);
	}
	

	@Override
	public int compareTo(AnswerIntegral o) {
		AnswerIntegral	aim=(AnswerIntegral) o;
		if(this.integral<aim.integral){
			return 1;
		}else if(this.integral>aim.integral){
			return -1;
		}else{
			return 0;
		}
	}
	
}
