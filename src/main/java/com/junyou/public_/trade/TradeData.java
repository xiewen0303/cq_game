package com.junyou.public_.trade;

import java.util.HashMap;
import java.util.Map;

import com.junyou.constants.GameConstants;

/**
 * 交易数据
 * @author wind
 * @email  18221610336@163.com
 * @date  2015-1-15 下午4:40:59
 */
public class TradeData {

	private long userRoleId;
	
	private String selfName = "";
	
	/**
	 * Key:物品GUID
	 * value:交易格的索引
	 */
	private Map<Long,Integer> goodsGuids;
	
	/**
	 * Key:物品guid
	 * value:物品为数量
	 */
	private Map<Long,Integer> goodsCounts;
	/**
	 * 元宝
	 */
	private long yb; 
	
	/**
	 * 状态  1：交易开始，2：锁定，3：确认
	 */
	private int state;
	
	/**
	 * 另一个交易用户ID
	 */
	private long otherRoleId;
	

	public TradeData(long userRoleId) {
		super();
		this.userRoleId = userRoleId;
		state = GameConstants.START_TRADE;
	}

	public long getUserRoleId() {
		return userRoleId;
	}

	public void setUserRoleId(long userRoleId) {
		this.userRoleId = userRoleId;
	}

	/**
	 * 获取自己的名字
	 * @return
	 */
	public String getSelfName() {
		return selfName;
	}

	public void setSelfName(String selfName) {
		this.selfName = selfName;
	}

	/**
	 * Key:物品GUID
	 * value:交易格的索引
	 */
	public Map<Long, Integer> getGoodsGuids() {
		return goodsGuids;
	}

	
	public Map<Long,Integer> getGoodsCounts(){
		return goodsCounts;
	}
	
	/**
	 * 加入交易物品guid
	 * @param guid
	 * @param tradeIndex
	 */
	public  void addGoodsGuids(Long guid,Integer tradeIndex){
		if(goodsGuids == null){
			goodsGuids = new HashMap<Long, Integer>();
		}
		
		//交易索引已存在那先删除
		if(goodsGuids.containsValue(tradeIndex)){
			removeGuid(tradeIndex);
		}
		
		goodsGuids.put(guid, tradeIndex);
	}
	
	/**
	 * 加入交易物品数量
	 * @param guid
	 * @param goodsCount
	 */
	public void addGoodsCount(Long guid,Integer goodsCount){
		if(goodsCounts == null){
			goodsCounts = new HashMap<Long, Integer>();
		}
		
		goodsCounts.put(guid, goodsCount);
	}
	
	
	/**
	 * 清除索引号上的物品
	 * @param index
	 */
	public void removeGuid(Integer index){
		if(goodsGuids != null){
			Long _guid = null;
			for (Long guid : goodsGuids.keySet()) {
				int tmpIndex = goodsGuids.get(guid);
				
				if(tmpIndex == index) {
					_guid = guid;
					break;
				}
			}
			goodsGuids.remove(_guid);
			goodsCounts.remove(_guid);
		}
	}
	
	public void setGoodsGuids(Map<Long, Integer> goodsGuids) {
		this.goodsGuids = goodsGuids;
	}

 

	public long getYb() {
		return yb;
	}

	public void setYb(long yb) {
		this.yb = yb;
	}
 

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public long getOtherRoleId() {
		return otherRoleId;
	}

	public void setOtherRoleId(long otherRoleId) {
		this.otherRoleId = otherRoleId;
	}
	/**
	 * 是否已经添加到交易框中了
	 * @param guid
	 * @return
	 */
	public boolean isContains(Long guid) {
		if(goodsGuids!=null){
			return goodsGuids.containsKey(guid);
		}
		return false;
	}

}
