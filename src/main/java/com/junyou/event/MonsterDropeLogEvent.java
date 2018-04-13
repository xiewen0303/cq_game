package com.junyou.event;

import com.alibaba.fastjson.JSONArray;
import com.junyou.log.LogPrintHandle;

/**
 * 场景内怪物掉落日志事件
 * 
 * @author LiNing
 * @email anne_0520@foxmail.com
 * @date 2014-12-24 下午5:31:54
 */
public class MonsterDropeLogEvent extends AbsGameLogEvent{

	private static final long serialVersionUID = 1L;
	
	private long userRoleId;
	private String roleName;
	private Integer mapId;
	private String monsterId;
	private JSONArray goodsList;
	
	/**
	 * 怪物掉落日志
	 * @param userRoleId 击杀者Id
	 * @param roleName 击杀名称
	 * @param mapId 地图Id
	 * @param monsterId 怪物Id
	 * @param goodsList 掉落物品[goodsId(物品Id)，count(数量)]...
	 */
	public MonsterDropeLogEvent(long userRoleId,String roleName, Integer mapId, String monsterId, JSONArray goodsList){
		super(LogPrintHandle.STAGE_MONSTER_DROP);
		
		if(roleName == null) roleName = LogPrintHandle.LINE_CHAR;
		
		this.userRoleId = userRoleId;
		this.roleName = roleName;
		this.mapId = mapId;
		this.monsterId = monsterId;
		this.goodsList = goodsList;
	}
	
	public JSONArray getGoodsList() {
		return goodsList;
	}

	public String getMonsterId() {
		return monsterId;
	}

	public Integer getMapId(){
		return mapId;
	}

	public String getRoleName() {
		return roleName;
	}

	public long getUserRoleId() {
		return userRoleId;
	}
}