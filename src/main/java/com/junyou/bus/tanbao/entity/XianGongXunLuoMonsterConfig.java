package com.junyou.bus.tanbao.entity;

import java.util.List;

import com.junyou.stage.model.core.stage.aoi.AoiPoint;
import com.junyou.utils.collection.ReadOnlyList;

/**
 * 仙宫巡逻怪配置
 * @author LiuYu
 * 2015-6-18 下午7:51:29
 */
public class XianGongXunLuoMonsterConfig {
	private String monsterId;
	
	private List<AoiPoint> lujing;

	public String getMonsterId() {
		return monsterId;
	}
	public void setMonsterId(String monsterId) {
		this.monsterId = monsterId;
	}
	public List<AoiPoint> getLujing() {
		return lujing;
	}
	public void setLujing(List<AoiPoint> lujing) {
		this.lujing = new ReadOnlyList<>(lujing);
	}
	
	
}
