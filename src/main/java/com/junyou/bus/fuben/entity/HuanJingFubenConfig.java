package com.junyou.bus.fuben.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.junyou.bus.stagecontroll.MapType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.gameconfig.checker.IGoodsCheckConfig;
import com.junyou.utils.collection.ReadOnlyMap;

/**
 * 多人副本配置加载
 * @author chenjianye
 *
 */
public class HuanJingFubenConfig extends AbsFubenConfig implements IGoodsCheckConfig{
	private int level;
	private String fubenName;
	private int mapId;
	private ReadOnlyMap<String, Integer> reward;
	private int jiangExp;
	private int jiangMoney;
	private int tuijian;
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getMapId() {
		return mapId;
	}
	public void setMapId(int mapId) {
		this.mapId = mapId;
	}
	
	public ReadOnlyMap<String, Integer> getReward() {
		return reward;
	}
	public void setReward(Map<String, Integer> reward) {
		this.reward = new ReadOnlyMap<>(reward);
	}
	
	public int getJiangExp() {
		return jiangExp;
	}
	public void setJiangExp(int jiangExp) {
		this.jiangExp = jiangExp;
	}
	public int getJiangMoney() {
		return jiangMoney;
	}
	public void setJiangMoney(int jiangMoney) {
		this.jiangMoney = jiangMoney;
	}
		
	public int getTuijian() {
		return tuijian;
	}
	public void setTuijian(int tuijian) {
		this.tuijian = tuijian;
	}
	
	public String getFubenName() {
		return fubenName;
	}
	public void setFubenName(String fubenName) {
		this.fubenName = fubenName;
	}
	@Override
	public short getExitCmd() {
		return InnerCmdType.B_EXIT_MORE_FUBEN;
	}
	@Override
	public int getMapType() {
		return MapType.MORE_FUBEN_MAP;
	}
	@Override
	public String getConfigName() {
		return "HuanJingFubenConfig--" + getId();
	}
	@Override
	public List<Map<String, Integer>> getCheckMap() {
		List<Map<String, Integer>> list = null;
		if(reward != null && reward.size() > 0){
			if(list == null){
				list = new ArrayList<>();
			}
			list.add(reward);
		}
		return list;
	}
	@Override
	public boolean isAutoProduct() {
		return true;
	}
	@Override
	public int getFubenType() {
		return GameConstants.FUBEN_TYPE_MORE;
	}
	

}
