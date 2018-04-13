package com.junyou.gameconfig.export;

import com.junyou.gameconfig.constants.DropIdType;

/**
 * 
 * @description 掉落 
 *
 * @author LiuJuan
 * @date 2012-3-16 上午10:20:28
 */
public class DropRule {
	
	private DropIdType dropIdType;
	private String dropId;
	private String componentDropId;
	private float dropRate;
	private int dropCount;
	
	/**
	 * 掉落编号类型(组包、物品)
	 * @param dropIdType
	 */
	public void setDropIdType(DropIdType dropIdType) {
		this.dropIdType = dropIdType;
	}

	/**
	 * 掉落物品编号
	 * @param dropId
	 */
	public void setDropId(String dropId) {
		this.dropId = dropId;
	}
	
	/**
	 * 掉落组包编号
	 * @param componentDropId
	 */
	public void setComponentDropId(String componentDropId) {
		this.componentDropId = componentDropId;
	}
	
	/**
	 * 掉落几率
	 * @param dropRate
	 */
	public void setDropRate(float dropRate) {
		this.dropRate = dropRate;
	}
	
	/**
	 * 掉落计算数量 - *
	 * @param dropCount
	 */
	public void setDropCount(int dropCount)  {
		this.dropCount = dropCount;
	}

	/**
	 * 掉落几率
	 */
	public float getDropRate() {
		
		return dropRate;
	}

	/**
	 * 掉落编号类型
	 */
	public DropIdType getDropIdType() {
		return dropIdType;
	}

	/**
	 * 掉落编号
	 */
	public String getDropId() {
		
		return dropId;
	}

	/**
	 * 掉落组包编号
	 */
	public String getComponentDropId() {
		
		return componentDropId;
	}

	/**
	 * 计算次数
	 */
	public int getDropCount() {
		
		return dropCount;
	}
}