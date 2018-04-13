package com.junyou.gameconfig.publicconfig.configure.export;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.junyou.bus.equip.configure.export.ShengJiBiaoConfig;
import com.junyou.gameconfig.constants.ModulePropIdConstant;
import com.junyou.log.LogPrintHandle;


/**
 * 神器洗练
 * @date 2015-7-8 上午9:37:35
 * sqxilian	神器洗炼	needvip1	当开启第一个锁定条目需要VIP等级	2:1|5:2|7:3|9:4		needgold	元宝洗炼一次消耗元宝	300		
 * needfu	每锁定一个条目消耗锁定符id和数量	9997:2		needshi	大禹石（魔血）	100013:1		jiage	大禹石价格	500		
 * needmoney	洗炼消耗银两	0:00:00																																																																									

 */
public class ShenQiXiLianPublicConfig extends AdapterPublicConfig{
	private String  needvip1;//当开启第一个锁定条目需要VIP等级
	private int  needgold;//元宝洗炼一次消耗元宝
	private Object[] needfu;//每锁定一个条目消耗锁定符id和数量
	private Object[] needshi;//大禹石（魔血）
	private int jiage; //大禹石价格
	private int needmoney; //洗炼消耗银两
	private Object[] needVips;
	
	private Object[] getNeedVips() {
		if(needVips==null){
			Object[] arr1 = needvip1.split("\\|");
			Object[] arr2 = new Object[arr1.length];
			for (int i = 0; i < arr1.length; i++) {
				String[] values = new String[2];
				String o  = (String)arr1[i];
				values = o.split(":");
				arr2[i]  = values;
			}
			needVips  = arr2;
		}
		return needVips;
	}
	/**
	 * 获取对应vip锁定条目数
	 * 返回0表示不可洗练
	 * @param needVips
	 */
	public int  getLockNum(int vip){
		if(vip<=0 || vip>10){
			return 0;
		}
		 Object[] arr =getNeedVips();
		 int _num =0;
		 for (int i = 0; i < arr.length; i++) {
			 Object[] obj  = (Object[])arr[i];
			 int _vip = Integer.parseInt((String)obj[0]);
			if(vip>=_vip){
				Object[] nextObj  =null;
				 int _nextVip  =0;
				if(i+1<arr.length){
					   nextObj  = (Object[])arr[i+1];
					   _nextVip = Integer.parseInt((String)nextObj[0]);
				}
				if(nextObj!=null){
					if(_vip<=vip && vip<_nextVip){
						 _num = Integer.parseInt((String)obj[1]);
						 break;
					}	
				}else{
					  obj  = (Object[])arr[arr.length-1];
					 _num = Integer.parseInt((String)obj[1]);
				}
				
			}
		}
		return _num;
	}
	
	public static void main(String[] args) {
		ShenQiXiLianPublicConfig shengJiBiaoConfig  = new ShenQiXiLianPublicConfig();
		shengJiBiaoConfig.setNeedvip1("2:1|5:2|7:3|9:4");
		for (int i = -5; i < 12; i++) {
			System.out.println("vip"+i+":"+shengJiBiaoConfig.getLockNum(i));
		}
		
	}
	public void setNeedVips(Object[] needVips) {
		this.needVips = needVips;
	}
	
	public String getNeedvip1() {
		return needvip1;
	}
	public void setNeedvip1(String needvip1) {
		this.needvip1 = needvip1;
	}
	public int getNeedgold() {
		return needgold;
	}
	public void setNeedgold(int needgold) {
		this.needgold = needgold;
	}
	public Object[] getNeedfu() {
		return needfu;
	}
	public void setNeedfu(Object[] needfu) {
		this.needfu = needfu;
	}
	public Object[] getNeedshi() {
		return needshi;
	}
	public void setNeedshi(Object[] needshi) {
		this.needshi = needshi;
	}
	public int getJiage() {
		return jiage;
	}
	public void setJiage(int jiage) {
		this.jiage = jiage;
	}
	public int getNeedmoney() {
		return needmoney;
	}
	public void setNeedmoney(int needmoney) {
		this.needmoney = needmoney;
	}

	
	
}
