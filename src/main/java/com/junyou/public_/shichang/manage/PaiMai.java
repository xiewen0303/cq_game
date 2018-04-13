package com.junyou.public_.shichang.manage;

import com.junyou.bus.bag.entity.RoleItem;
 
/**
 * 拍卖上架纪录
 * @author wind
 * @email  18221610336@163.com
 * @date  2015-4-9 上午11:32:10
 */
public class PaiMai implements Comparable<PaiMai> {
	
	private RoleItem roleItem; 
 
	private Long userRoleId;
 
	private Integer price;
 
	private String roleName; 
 
	private Integer sellType;
	
	private long sellTime;//拍卖上架时间
	
	public long getSellTime() {
		return sellTime;
	}

	public void setSellTime(long sellTime) {
		this.sellTime = sellTime;
	}

	public Long getUserRoleId() {
		return userRoleId;
	}

	public void setUserRoleId(Long userRoleId) {
		this.userRoleId = userRoleId;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Integer getSellType() {
		return sellType;
	}

	public void setSellType(Integer sellType) {
		this.sellType = sellType;
	}

	public RoleItem getRoleItem() {
		return roleItem;
	}

	public void setRoleItem(RoleItem roleItem) {
		this.roleItem = roleItem;
	}
	
	public int getCount(){
		return roleItem.getCount();
	}
	
	public String getGoodsId(){
		return roleItem.getGoodsId();
	}
	
	public long getGuid(){
		return roleItem.getId();
	}

	@Override
	public int compareTo(PaiMai o) {
		// TODO Auto-generated method stub
		return 0;
	}
}
