package com.junyou.bus.tuangou.configure;

import java.util.Map;

/**
 * 团购
 * @author zhongdian
 * 2016年1月11日 下午2:32:47
 */
public class TuanGouConfig {
	
	private Map<String, Integer> jiangitem;
	private Map<String, Integer> jiangitem1;
	private Integer price;//幸运大礼价值
	private Integer price1;
	private Integer price2;//团购价格
	private Integer count;//全服可购买次数
	private String time1;
	private String time2;
	private String time3;
	private Integer cishu;//个人最多可购买次数
	public Map<String, Integer> getJiangitem() {
		return jiangitem;
	}
	public void setJiangitem(Map<String, Integer> jiangitem) {
		this.jiangitem = jiangitem;
	}
	public Map<String, Integer> getJiangitem1() {
		return jiangitem1;
	}
	public void setJiangitem1(Map<String, Integer> jiangitem1) {
		this.jiangitem1 = jiangitem1;
	}
	public Integer getPrice() {
		return price;
	}
	public void setPrice(Integer price) {
		this.price = price;
	}
	public Integer getPrice1() {
		return price1;
	}
	public void setPrice1(Integer price1) {
		this.price1 = price1;
	}
	public Integer getPrice2() {
		return price2;
	}
	public void setPrice2(Integer price2) {
		this.price2 = price2;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public String getTime1() {
		return time1;
	}
	public void setTime1(String time1) {
		this.time1 = time1;
	}
	public String getTime2() {
		return time2;
	}
	public void setTime2(String time2) {
		this.time2 = time2;
	}
	public String getTime3() {
		return time3;
	}
	public void setTime3(String time3) {
		this.time3 = time3;
	}
	public Integer getCishu() {
		return cishu;
	}
	public void setCishu(Integer cishu) {
		this.cishu = cishu;
	}
	
	
	
	
}
