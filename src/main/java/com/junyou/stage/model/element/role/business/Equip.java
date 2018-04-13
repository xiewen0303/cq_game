package com.junyou.stage.model.element.role.business;

import java.io.Serializable;

import com.junyou.utils.common.CovertObjectUtil;

public class Equip implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Long id;	//guid
	private String goodsId; //物品Id
	private int qianghuaLevel;//强化等级
	private int slot;		//格位号
	private long expireTime; //过期时间
	private Integer randomAttrId;//出生时生成随机属性 
	private Integer zhuShenLevel;//套装铸神等级
	private int tpVal;//提品值
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}
	public int getQianghuaLevel() {
		return qianghuaLevel;
	}
	public void setQianghuaLevel(int qianghuaLevel) {
		this.qianghuaLevel = qianghuaLevel;
	}
	public int getSlot() {
		return slot;
	}
	public void setSlot(int slot) {
		this.slot = slot;
	}
	public long getExpireTime() {
		return expireTime;
	}
	public void setExpireTime(long expireTime) {
		this.expireTime = expireTime;
	}
	
	public Integer getRandomAttrId() {
		return randomAttrId;
	}
	public void setRandomAttrId(Integer randomAttrId) {
		this.randomAttrId = randomAttrId;
	}
	
	public Integer getZhuShenLevel() {
		return zhuShenLevel;
	}
	public void setZhuShenLevel(Integer zhuShenLevel) {
		this.zhuShenLevel = zhuShenLevel;
	}
	
	public int getTpVal() {
		return tpVal;
	}
	public void setTpVal(int tpVal) {
		this.tpVal = tpVal;
	}
	public static Equip convert2Equip(Object[] equipData){
		//equip.getGuid(),equip.getGoodsId(),equip.getQianhuaLevel(),equip.getSlot(),equip.getExpireTime(),equip.getRandomAttrs(),equip.getZhushenLevel()
		Equip equip = new Equip();
		equip.setId((Long)equipData[0]);
		equip.setGoodsId((String)equipData[1]);
		equip.setQianghuaLevel( (Integer)equipData[2] ); 
		equip.setSlot((Integer)equipData[3]);
		
		equip.setExpireTime((Long)equipData[4]);
		equip.setRandomAttrId((Integer)equipData[5]);
		equip.setZhuShenLevel((Integer)equipData[6]);
		if(equipData.length > 7){
			equip.setTpVal(CovertObjectUtil.object2int(equipData[7]));	
		}
		return equip;
	}
}
