package com.junyou.bus.bagua.configure.export;

import java.util.Map;

import com.junyou.bus.bagua.constants.BaguaConstant;
import com.junyou.bus.fuben.entity.AbsFubenConfig;
import com.junyou.bus.maigu.configure.constants.MaiguConstant;
import com.junyou.bus.stagecontroll.MapType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;

public class DuoRenTongYongBiaoConfig extends AbsFubenConfig {
	private int type;
	private String name;
	private int level;
	private Map<String, Integer> jiangliMap;
	private int mapId;
	private int tuijian;
	private int count;
	private long jiangexp;
	private int jiangmoney;
	private int xianzhizhanli;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public Map<String, Integer> getJiangliMap() {
		return jiangliMap;
	}

	public void setJiangliMap(Map<String, Integer> jiangliMap) {
		this.jiangliMap = jiangliMap;
	}

	public int getMapId() {
		return mapId;
	}

	public void setMapId(int mapId) {
		this.mapId = mapId;
	}

	public int getTuijian() {
		return tuijian;
	}

	public void setTuijian(int tuijian) {
		this.tuijian = tuijian;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public long getJiangexp() {
		return jiangexp;
	}

	public void setJiangexp(long jiangexp) {
		this.jiangexp = jiangexp;
	}

	public int getJiangmoney() {
		return jiangmoney;
	}

	public void setJiangmoney(int jiangmoney) {
		this.jiangmoney = jiangmoney;
	}

	public int getXianzhizhanli() {
		return xianzhizhanli;
	}

	public void setXianzhizhanli(int xianzhizhanli) {
		this.xianzhizhanli = xianzhizhanli;
	}

	@Override
	public int getMapType() {
		if (type == BaguaConstant.BUAGUA_FUBEN) {
			return MapType.BAGUA_FUBEN_MAP;
		}
		if (type == MaiguConstant.MAIGU_FUBEN) {
			return MapType.MAIGU_FUBEN_MAP;
		}
		throw new RuntimeException("error type" + type);
	}

	@Override
	public int getFubenType() {
		if (type == BaguaConstant.BUAGUA_FUBEN) {
			return GameConstants.FUBEN_TYPE_BAGUA;
		}
		if (type == MaiguConstant.MAIGU_FUBEN) {
			return GameConstants.FUBEN_TYPE_MAIGU;
		}
		throw new RuntimeException("error type" + type);
	}

	@Override
	public short getExitCmd() {
		if (type == BaguaConstant.BUAGUA_FUBEN) {
			return InnerCmdType.BAGUA_FORCE_EXIT_FUBEN;
		}
		if (type == MaiguConstant.MAIGU_FUBEN) {
			return InnerCmdType.MAIGU_FORCE_EXIT_FUBEN;
		}
		throw new RuntimeException("error type" + type);
	}

	@Override
	public boolean isAutoProduct() {
		return true;
	}

	public String getMonster() {
		for (String e : getWantedMap().keySet()) {
			return e;
		}
		return null;
	}

}
