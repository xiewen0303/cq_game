package com.junyou.bus.zuoqi.configure.export;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kernel.data.dao.AbsVersion;

/**
 * 
 * @description 坐骑升阶表 
 *
 * @author wind
 * @date 2015-03-31 19:07:18
 */
public class YuJianJiChuConfig extends AbsVersion{
	private int star; //星级
	
	private int showId;
	
	private Map<int[],Integer> weights = new HashMap<>();
	
	private int level;
	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	/**
	 * 消耗物品优先级
	 */
	private List<String> consumeItems ;//消耗物品优先于索引
	
	private Map<String,Long> attrs ;//基础属性
	
	private Long moveAttrVal; //移动属性

	private Integer czdopen;

	private Integer gold;//元宝抵消道具
	private Integer bgold;//绑定元宝抵消道具
	
	private String czdid;

	private Integer qndmax; 

	private String value1;

	private Integer id;

	private Integer levelmax;

	private String ggid;

	private String name; 

	private Integer money; 

	private float cztime; 

	private boolean ggopen;

	private Integer zfzmin; 

	private Integer zfzmax;
	
	private Integer zfzmin2;
	
	private Integer zfzmin3;

	private String qndid; 
	
	private boolean zfztime;

	private Integer number;

	private Integer czdmax;

	private Integer qndopen;
     
	private Integer pro;


	private int jinengge; 
	
	private String mallid;  //商城售卖物品的信息
	
	private int realNeedZfz;
	
	public int getRealNeedZfz() {
		return realNeedZfz;
	}

	public void setRealNeedZfz(int realNeedZfz) {
		this.realNeedZfz = realNeedZfz;
	}

	public String getMallid() {
		return mallid;
	}

	public void setMallid(String mallid) {
		this.mallid = mallid;
	}

	public boolean isGgopen() {
		return ggopen;
	}

	public void setGgopen(boolean ggopen) {
		this.ggopen = ggopen;
	}

	public float getCztime() {
		return cztime;
	}

	public void setCztime(float cztime) {
		this.cztime = cztime;
	}

	public Long getMoveAttrVal() {
		return moveAttrVal;
	}

	public void setMoveAttrVal(Long moveAttrVal) {
		this.moveAttrVal = moveAttrVal;
	}

	public List<String> getConsumeItems() {
		return consumeItems;
	}

	public void setConsumeItems(List<String> consumeItems) {
		this.consumeItems = consumeItems;
	}

	public Map<String, Long> getAttrs() {
		return attrs;
	}

	public void setAttrs(Map<String, Long> attrs) {
		this.attrs = attrs;
	}

	public Integer getCzdopen() {
		return czdopen;
	}

	public void setCzdopen(Integer czdopen) {
		this.czdopen = czdopen;
	}

	public String getCzdid() {
		return czdid;
	}

	public void setCzdid(String czdid) {
		this.czdid = czdid;
	}

	public Integer getQndmax() {
		return qndmax;
	}

	public void setQndmax(Integer qndmax) {
		this.qndmax = qndmax;
	}

	public String getValue1() {
		return value1;
	}

	public void setValue1(String value1) {
		this.value1 = value1;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getLevelmax() {
		return levelmax;
	}

	public void setLevelmax(Integer levelmax) {
		this.levelmax = levelmax;
	}

	public String getGgid() {
		return ggid;
	}

	public void setGgid(String ggid) {
		this.ggid = ggid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getMoney() {
		return money;
	}

	public void setMoney(Integer money) {
		this.money = money;
	}
	  
	public Integer getZfzmin3() {
		return zfzmin3;
	}

	public void setZfzmin3(Integer zfzmin3) {
		this.zfzmin3 = zfzmin3;
	}

	public Integer getZfzmin2() {
		return zfzmin2;
	}

	public void setZfzmin2(Integer zfzmin2) {
		this.zfzmin2 = zfzmin2;
	}
	

	public String getQndid() {
		return qndid;
	}

	public void setQndid(String qndid) {
		this.qndid = qndid;
	}
 

	public boolean isZfztime() {
		return zfztime;
	}

	public void setZfztime(boolean zfztime) {
		this.zfztime = zfztime;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public Integer getCzdmax() {
		return czdmax;
	}

	public void setCzdmax(Integer czdmax) {
		this.czdmax = czdmax;
	}

	public Integer getQndopen() {
		return qndopen;
	}

	public void setQndopen(Integer qndopen) {
		this.qndopen = qndopen;
	}
 

	public Integer getPro() {
		return pro;
	}

	public void setPro(Integer pro) {
		this.pro = pro;
	} 
 
	public int getJinengge() {
		return jinengge;
	}

	public void setJinengge(int jinengge) {
		this.jinengge = jinengge;
	}

	public Integer getZfzmin() {
		return zfzmin;
	}

	public void setZfzmin(Integer zfzmin) {
		this.zfzmin = zfzmin;
	}

	public Integer getZfzmax() {
		return zfzmax;
	}

	public void setZfzmax(Integer zfzmax) {
		this.zfzmax = zfzmax;
	}
	
	public Integer getGold() {
		return gold;
	}

	public void setGold(Integer gold) {
		this.gold = gold;
	}

	public Integer getBgold() {
		return bgold;
	}

	public void setBgold(Integer bgold) {
		this.bgold = bgold;
	}

	public int getStar() {
		return star;
	}

	public void setStar(int star) {
		this.star = star;
	}

	public int getShowId() {
		return showId;
	}

	public void setShowId(int showId) {
		this.showId = showId;
	}

	public Map<int[], Integer> getWeights() {
		return weights;
	}

	public void setWeights(Map<int[], Integer> weights) {
		this.weights = weights;
	}
	
	
}
