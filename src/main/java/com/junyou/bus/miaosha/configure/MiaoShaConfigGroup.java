package com.junyou.bus.miaosha.configure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author LiuYu
 * 2016-3-4 下午3:46:20
 */
public class MiaoShaConfigGroup {
	private int subId;
	private Map<Integer,MiaoShaConfig> configs = new HashMap<>();
	private String des;
	private String pic;
	private String openTime;
	private String md5Version;
	private List<MiaoShaTime> timeList = new ArrayList<>();
	public int getSubId() {
		return subId;
	}
	public void setSubId(int subId) {
		this.subId = subId;
	}
	public Map<Integer, MiaoShaConfig> getConfigs() {
		return configs;
	}
	public void addConfig(MiaoShaConfig config){
		configs.put(config.getId(), config);
		timeList.add(config.getTime());
	}
	public String getDes() {
		return des;
	}
	public void setDes(String des) {
		this.des = des;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public MiaoShaConfig getConfig(long cur){
		for (MiaoShaTime time : timeList) {
			if(time.getEndTime() > cur){
				return configs.get(time.getId());
			}
		}
		return null;
		/*MiaoShaConfig config = null;
		for (MiaoShaTime time : timeList) {
			if(time.getEndTime() > cur){
				return configs.get(time.getId());
			}else{
				if(config == null){
					config = configs.get(time.getId());
				}else{
					if(time.getEndTime() > config.getTime().getEndTime()){
						config = configs.get(time.getId());
					}
				}
			}
		}
		return config;
		*/
	}
	public void sort(){
		Collections.sort(timeList);
	}
	public String getMd5Version() {
		return md5Version;
	}
	public void setMd5Version(String md5Version) {
		this.md5Version = md5Version;
	}
	public String getOpenTime() {
		return openTime;
	}
	public void setOpenTime(String openTime) {
		this.openTime = openTime;
	}
}
