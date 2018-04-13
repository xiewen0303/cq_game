package com.junyou.bus.bag.export;

import java.util.Map;

import com.junyou.bus.bag.entity.RoleItem;
import com.junyou.utils.common.CovertObjectUtil;
import com.junyou.utils.common.ObjectUtil;

 
/**
 * 物品对外实例
 * @author: wind
 * @email: 18221610336@163.com
 * @version: 2014-12-26下午2:55:24
 * @Description:
 */
public class RoleItemExport{
	
	private RoleItem roleItem;
	
	public RoleItemExport(RoleItem roleItem) {
		super();
		this.roleItem = roleItem;
	}
	public int getSlot(){
		return roleItem.getSlot();
	}

	public long getGuid() {
		return roleItem.getId();
	}
 
	public String getGoodsId() {
		return roleItem.getGoodsId();
	}

	public int getCount() {
		return roleItem.getCount();
	}

//	public String getGems() {
//		return roleItem.getGems();
//	} 
	
	public Long getExpireTime() {
		return roleItem.getExpireTime();
	}
 
//	public int getJinyouLevel() {
//		return roleItem.getJinyouLevel();
//	} 

	public Integer getQianhuaLevel() {
		return roleItem.getQianhuaLevel();
	}
	public Integer getZhushenLevel() {
		return roleItem.getZhushenLevel();
	}
	public Integer getZhushenFailTimes() {
		return roleItem.getZhushenFailTimes();
	}

//	public String getJianding() {
//		return roleItem.getJianding();
//	}
// 
//	public Integer getOpenKongCount() {
//		return roleItem.getOpenKongCount() == null ? 0 : roleItem.getOpenKongCount();
//	} 
 
	public long getItemCreateTime() {
		return roleItem.getCreateTime();
	}
	
	/**
	 * 是否过期
	 * @return
	 */
	public boolean isExpireTime() {
		return roleItem.isExpireTimed();
	}
	
	
	public Map<Integer,Object> getOtherData(){
		return roleItem.getOtherDataMap();
	}
	
	public String getOtherDataStr(){
		return roleItem.getOtherData();
	}
	
	/**
	 * 获取其他类型数据
	 * @param otherDataType
	 * @return
	 */
	public Object getOtherData(int otherDataType){
		Map<Integer,Object> otherData = roleItem.getOtherDataMap();
		if(otherData!=null){
			return otherData.get(otherDataType);
		}
		return null;
	}
	
	public Integer getRandomAttrs(){
		return roleItem.getRandomAttrs();
	}
	
	public Integer getTipinValue() {
		return CovertObjectUtil.object2int(roleItem.getTipinValue());
	}
}