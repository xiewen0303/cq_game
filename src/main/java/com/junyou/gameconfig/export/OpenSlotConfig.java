package com.junyou.gameconfig.export;


/**
 * 开格子配置
 * 
 * @author DaoZheng Yuan
 * @created 2012-3-15下午6:29:17
 */
public class OpenSlotConfig {

	private String primary;
	
	private Integer id;

	private Integer gold;

	public Integer getId() {
		return id;
	}

	public Integer getGold() {
		return gold;
	}

	public void setGold(Integer gold) {
		this.gold = gold;
	}

	public void setId(Integer id) {
		this.id = id;
		primary = id+"";
	}

	public String getPirmaryKeyName() {
		return "id";
	}

	public String getPrimaryKeyValue() {
		return primary;
	}

}
