package com.junyou.bus.dati.configure;

import java.util.ArrayList;
import java.util.List;

public class TiMuConfig implements Cloneable {
	/**题库中题目ID*/
	private int id;

	private String title;

	/**
	 * 答案选项 String:选项内容
	 * */
	private List<String> answers;

	/** 正确答案索引 */
	private int right;

	private Long startTime;

	private Long endTime;
	/**第几题*/
	private int number;
	/**下一题ID*/
	private int nextTiId;
	
	public TiMuConfig() {
		this.answers = new ArrayList<>(4);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getRight() {
		return right;
	}

	public void setRight(int right) {
		this.right = right;
	}

	public Long getStartTime() {
		return startTime;
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	public Long getEndTime() {
		return endTime;
	}

	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}

	public List<String> getAnswers() {
		return answers;
	}

	public void setAnswers(List<String> answers) {
		this.answers = answers;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public int getNextTiId() {
		return nextTiId;
	}

	public void setNextTiId(int nextTiId) {
		this.nextTiId = nextTiId;
	}

	
	@Override
	public TiMuConfig clone() throws CloneNotSupportedException {

		TiMuConfig clone=(TiMuConfig)super.clone();
		List<String> newList=new ArrayList<>(4);
		for (String opt : this.answers) {
			newList.add(opt);
		}
		clone.setAnswers(newList);
		return clone;
	}
	
	
	
	private Object[] sendData ;
	private Object[] answersArray ;
	
	public  Object[] getMsgData(){
		if(sendData==null){
			sendData = new Object[5];
			sendData[0] = getId();
			sendData[1] = getNumber();
			sendData[2] = getTitle();
			if(answersArray==null){
				answersArray=getAnswers().toArray();
			}
			sendData[3] = answersArray;
			sendData[4] = getEndTime();
		}
	
		return sendData;
	}
}
