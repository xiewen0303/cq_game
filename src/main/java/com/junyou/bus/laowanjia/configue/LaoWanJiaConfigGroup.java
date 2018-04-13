package com.junyou.bus.laowanjia.configue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @description 老玩家回归活动配置表 
 *
 * @author ZHONGDIAN
 * @date 2013-12-12 11:43:48
 */
public class LaoWanJiaConfigGroup {
	
	private Map<Integer, LaoWanJiaConfig> configMap = new HashMap<>();
	private String des;//活动描述
	private String pic;//背景图
	private Integer weiLogin;//未登陆几天以上
	private List<Object[]> dengLuVo;
	
	
	public List<Object[]> getDengLuVo() {
		return dengLuVo;
	}

	public void setDengLuVo(List<Object[]> dengLuVo) {
		this.dengLuVo = dengLuVo;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public Integer getWeiLogin() {
		return weiLogin;
	}

	public void setWeiLogin(Integer weiLogin) {
		this.weiLogin = weiLogin;
	}

	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}

	private String md5Version;
	
	public String getMd5Version() {
		return md5Version;
	}

	public void setMd5Version(String md5Version) {
		this.md5Version = md5Version;
	}

	public Map<Integer, LaoWanJiaConfig> getConfigMap() {
		return configMap;
	}

	public void setConfigMap(Map<Integer, LaoWanJiaConfig> configMap) {
		this.configMap = configMap;
	}
	
	
}
