package com.junyou.bus.fuben.entity;

import com.junyou.bus.stagecontroll.MapType;
import com.junyou.cmd.ClientCmdType;
import com.junyou.constants.GameConstants;

/**
 * @author LiuYu
 * 2015-6-11 上午11:32:20
 */
public class PataConfig  extends AbsFubenConfig{
	private int money;
	private long exp;
	private long zq;
	
	public int getMoney() {
		return money;
	}
	public void setMoney(int money) {
		this.money = money;
	}
	public long getExp() {
		return exp;
	}
	public void setExp(long exp) {
		this.exp = exp;
	}
	public long getZq() {
		return zq;
	}
	public void setZq(long zq) {
		this.zq = zq;
	}
	@Override
	public int getMapType() {
		return MapType.PATA_FUBEN_MAP;
	}
	@Override
	public int getFubenType() {
		return GameConstants.FUBEN_TYPE_PATA;
	}
	@Override
	public short getExitCmd() {
		return ClientCmdType.EXIT_PATA;
	}
	@Override
	public boolean isAutoProduct() {
		return false;
	}
	
}
