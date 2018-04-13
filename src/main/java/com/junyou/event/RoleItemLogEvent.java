package com.junyou.event;

import com.junyou.log.ChuanQiLog;

/**
 * 物品操作日志(物品进容器,出容器日志记录事件)
 * 
 * @author wind
 * 
 */
public class RoleItemLogEvent extends AbsGameLogEvent {

	private static final long serialVersionUID = 1L;

	private Long userRoleId;
	private Long guid;
	private String roleName;
	private String goodsId;
	private Integer count;
	private Integer sourceType;// 获得消耗来源
	private Object bz;//备注(物品获得来源的父类guid,或者其他信息)
	
	public RoleItemLogEvent(int type, Long userRoleId, Long guid,
			String roleName, String goodsId, Integer count, Integer sourceType,Object bz) {
		super(type);
		this.userRoleId = userRoleId;
		this.guid = guid;
		this.roleName = roleName;
		this.goodsId = goodsId;
		this.count = count;
		this.sourceType = sourceType;
		this.bz = bz;
		if(sourceType == null || sourceType < 1){
			ChuanQiLog.error("类型错误,玩家名：{},物品id:{},物品数量:{}",roleName,goodsId,count);
		}
	}

	public Long getUserRoleId() {
		return userRoleId;
	}

	public Long getGuid() {
		return guid;
	}

	public String getRoleName() {
		return roleName;
	}

	public String getGoodsId() {
		return goodsId;
	}

	public Integer getCount() {
		return count;
	}

	public Integer getSourceType() {
		return sourceType;
	}

	public Object getBz() {
		return bz;
	}
}