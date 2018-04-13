package com.junyou.public_.guild.entity;

/**
 * 招收条件
 * @author LiuYu
 * @date 2015-1-21 下午4:20:49
 */
public class GuildZhaoShouConfig {
	private int id;
	private int type;//1等级2VIP
	private int value;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	
}
