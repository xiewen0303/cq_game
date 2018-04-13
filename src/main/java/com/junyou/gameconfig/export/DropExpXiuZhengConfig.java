package com.junyou.gameconfig.export;

import com.kernel.data.dao.IEntity;

/**
 * 
 * @description 掉落经验血量修正值
 *
 * @author LiuJuan
 * @date 2012-3-30 下午1:32:13
 */
public class DropExpXiuZhengConfig {

	private String primary;
	private int harmPercent;
	private float xiuZhengPercent;
	private float teamXiuZhengPercent;
	
	public int getHarmPercent() {
		return harmPercent;
	}

	public void setHarmPercent(int harmPercent) {
		this.harmPercent = harmPercent;
		this.primary = harmPercent+"";
	}

	public float getXiuZhengPercent() {
		return xiuZhengPercent;
	}

	public void setXiuZhengPercent(float xiuZhengPercent) {
		this.xiuZhengPercent = xiuZhengPercent;
	}

	public float getTeamXiuZhengPercent() {
		return teamXiuZhengPercent;
	}

	public void setTeamXiuZhengPercent(float teamXiuZhengPercent) {
		this.teamXiuZhengPercent = teamXiuZhengPercent;
	}

	public String getPirmaryKeyName() {
		
		return "harmPercent";
	}

	public String getPrimaryKeyValue() {
		
		return primary;
	}

	public IEntity copy() {
		// TODO Auto-generated method stub
		return null;
	}

}
