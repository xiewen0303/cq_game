package com.junyou.bus.shouchong.configure;

import java.util.Map;

import com.junyou.configure.vo.GoodsConfigureVo;

/**
 * 类首充活动配置
 */
public class ShouChongActivityConfig {
	//配置Id
	private int configId;
	
	//需要的元宝
	private int needYb;
	
	//礼包名称
	private String name;
	
	//奖励物品-服务自己用的(通用奖励和职业奖励已合并)
	private Map<Byte,Map<String,GoodsConfigureVo>> jianLiMap;
	
	//奖励物品-给客户端用的(通用奖励和职业奖励已合并)
	private Map<Byte,Object[]> jianLiClientMap;
	
	//资源
	private Map<Byte, String> resClientMap;
	
	//美术字
	private Map<Byte, String> bgClientMap;
	
	//显示装备资源
	private Map<Byte,String> showMap;
	
	//按钮显示0
	private String btn0;

	//按钮显示1
	private String btn1;
	
	//全服公告
	private int gongGao;
	
	/**
	 * 是否有公告
	 * @return true:有,false:没有
	 */
	public boolean isHaveGongGao(){
		if(gongGao != 0){
			return true;
		}else{
			return false;
		}
	}
	
	public int getGongGao() {
		return gongGao;
	}

	public void setGongGao(int gongGao) {
		this.gongGao = gongGao;
	}

	public int getNeedYb() {
		return needYb;
	}

	public void setNeedYb(int needYb) {
		this.needYb = needYb;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Map<String, GoodsConfigureVo> getJianLiMapByJob(byte job) {
		if(jianLiMap == null){
			return null;
		}
		return jianLiMap.get(job);
	}

	public void setJianLiMap(Map<Byte, Map<String, GoodsConfigureVo>> jianLiMap) {
		this.jianLiMap = jianLiMap;
	}
	
	
	
	/**
	 * 根据job获取客户端奖励数据
	 * @param job
	 * @return
	 */
	public Object[] getJianLiClientDataByJob(byte job){
		if(jianLiClientMap == null){
			return null;
		}else{
			return jianLiClientMap.get(job);
		}
	}
	

	public void setJianLiClientMap(Map<Byte, Object[]> jianLiClientMap) {
		this.jianLiClientMap = jianLiClientMap;
	}

	public String getResStringByJob(byte job){
		if(resClientMap == null){
			return null;
		}else{
			return resClientMap.get(job);
		}
	}

	public void setResClientMap(Map<Byte, String> resClientMap) {
		this.resClientMap = resClientMap;
	}

	public String getBgStringByJob(byte job){
		if(bgClientMap == null){
			return null;
		}else{
			return bgClientMap.get(job);
		}
	}
	
	public void setBgClientMap(Map<Byte, String> bgClientMap) {
		this.bgClientMap = bgClientMap;
	}
	
	
	public String getShowMapByJob(byte job) {
		if(showMap == null){
			return null;
		}else{
			return showMap.get(job);
		}
	}

	public void setShowMap(Map<Byte, String> showMap) {
		this.showMap = showMap;
	}

	public String getBtn0() {
		return btn0;
	}

	public void setBtn0(String btn0) {
		this.btn0 = btn0;
	}

	public String getBtn1() {
		return btn1;
	}

	public void setBtn1(String btn1) {
		this.btn1 = btn1;
	}

	public int getConfigId() {
		return configId;
	}

	public void setConfigId(int configId) {
		this.configId = configId;
	}
}
