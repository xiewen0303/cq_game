package com.junyou.bus.xunbao.configure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 
 * @author zhongdian 
 * @email  zhongdian@junyougame.com
 * @date 2015-10-8 下午2:24:31
 */
public class RfbXunBaoConfigGroup {
	
	private Map<Integer,RfbXunBaoConfig> xunBaoConfigs;
	
	private String pic;
	
	private String des;//活动描述
	
	private Map<Integer,Map<Integer,Integer>> typeCount=new HashMap<>();
	
	private List<Object> typeCountClient = new ArrayList<Object>();
	
	private Map<Integer, AllPeopleRewardsConfig> allPeopleRewardsConfigs = new HashMap<>();
	//全服寻宝总次数
	private Integer allXunbaoCount = -1;
	
	private Object[] showGoods;
	
	private String md5Version;
	
	private Integer maxCount = 9999;
	
	public Map<Integer, RfbXunBaoConfig> getXunBaoConfigs() {
		return xunBaoConfigs;
	}

	public void setXunBaoConfigs(Map<Integer, RfbXunBaoConfig> xunBaoConfigs) {
		this.xunBaoConfigs = xunBaoConfigs;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}

	public String getMd5Version() {
		return md5Version;
	}

	public void setMd5Version(String md5Version) {
		this.md5Version = md5Version;
	} 
	
	public Map<Integer,Map<Integer,Integer>> getTypeCount() {
		return typeCount;
	}
	
	public void addTypeCount(int id, int key,int value){
		Map<Integer, Integer> map = new HashMap<>();
		map.put(key, value);
		typeCount.put(id, map);
		typeCountClient.add(new Object[]{key, value, id});
	}

	public Integer getMaxCount() {
		return maxCount;
	}

	public void setMaxCount(Integer maxCount) {
		this.maxCount = maxCount;
	}

	public List<Object> getTypeCountClient() {
		return typeCountClient;
	}

	public void setTypeCountClient(List<Object> typeCountClient) {
		this.typeCountClient = typeCountClient;
	}

	public Integer getAllXunbaoCount() {
		return allXunbaoCount;
	}

	public void setAllXunbaoCount(Integer allXunbaoCount) {
		this.allXunbaoCount = allXunbaoCount;
	}
	public void addAllXunbaoCount() {
		this.allXunbaoCount++;
	}
	public void addAllXunbaoCount(int count) {
		this.allXunbaoCount += count;
	}

	public Object[] getShowGoods() {
		return showGoods;
	}

	public void setShowGoods(Object[] showGoods) {
		this.showGoods = showGoods;
	}
	
	public Map<Integer, AllPeopleRewardsConfig> getAllPeopleRewardsConfigs() {
		return allPeopleRewardsConfigs;
	}

	public void addAllPeopleRewardsConfigs(Integer id, Integer totalCount, String goods, Integer goodsCount) {
		allPeopleRewardsConfigs.put(id,new AllPeopleRewardsConfig(id,totalCount,goods,goodsCount));
	}

	public class AllPeopleRewardsConfig{
		private Integer id;
		//全民奖励需要次数
		private Integer totalCount;
		private String goodsId;
		private Integer goodsCount;
		
		public AllPeopleRewardsConfig(Integer id, Integer totalCount, String goodsId, Integer goodsCount) {
			super();
			this.id = id;
			this.totalCount = totalCount;
			this.goodsId = goodsId;
			this.goodsCount = goodsCount;
		}
		public Integer getId() {
			return id;
		}
		public void setId(Integer id) {
			this.id = id;
		}
		public Integer getTotalCount() {
			return totalCount;
		}
		public void setTotalCount(Integer totalCount) {
			this.totalCount = totalCount;
		}
		public String getGoodsId() {
			return goodsId;
		}
		public void setGoodsId(String goodsId) {
			this.goodsId = goodsId;
		}
		public Integer getGoodsCount() {
			return goodsCount;
		}
		public void setGoodsCount(Integer goodsCount) {
			this.goodsCount = goodsCount;
		}
	}
}
