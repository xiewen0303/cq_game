package com.junyou.bus.happycard.entity;
import java.io.Serializable;

import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

@Table("refabu_happy_card")
public class RefabuHappyCard extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("id")
	private Long id;

	@Column("sub_id")
	private Integer subId;

	@Column("user_role_id")
	private Long userRoleId;
	
	@Column("before_yb")
	private Integer beforeYb;

	@Column("items")
	private String items;
	
	@Column("item_index")
	private String itemIndex;

	@Column("fan_times")
	private Integer fanTimes;
	
	@Column("multi")
	private Integer multi;

	@Column("update_time")
	private Long updateTime;


	public Long getId(){
		return id;
	}

	public  void setId(Long id){
		this.id = id;
	}

	public Integer getSubId(){
		return subId;
	}

	public  void setSubId(Integer subId){
		this.subId = subId;
	}

	public Long getUserRoleId(){
		return userRoleId;
	}

	public  void setUserRoleId(Long userRoleId){
		this.userRoleId = userRoleId;
	}

	public String getItems(){
		return items;
	}

	public  void setItems(String items){
		this.items = items;
	}

	public Integer getFanTimes(){
		return fanTimes;
	}

	public  void setFanTimes(Integer fanTimes){
		this.fanTimes = fanTimes;
	}

	public Long getUpdateTime(){
		return updateTime;
	}

	public  void setUpdateTime(Long updateTime){
		this.updateTime = updateTime;
	}

	public Integer getBeforeYb() {
		return beforeYb;
	}

	public void setBeforeYb(Integer beforeYb) {
		this.beforeYb = beforeYb;
	}

	@Override
	public String getPirmaryKeyName() {
		return "id";
	}

	@Override
	public Long getPrimaryKeyValue() {
		return id;
	}

	public Integer getMulti() {
		return multi;
	}

	public void setMulti(Integer multi) {
		this.multi = multi;
	}

	public String getItemIndex() {
		return itemIndex;
	}

	public void setItemIndex(String itemIndex) {
		this.itemIndex = itemIndex;
	}

	public RefabuHappyCard copy(){
		RefabuHappyCard result = new RefabuHappyCard();
		result.setId(getId());
		result.setSubId(getSubId());
		result.setUserRoleId(getUserRoleId());
		result.setBeforeYb(getBeforeYb());
		result.setMulti(getMulti());
		result.setItems(getItems());
		result.setItemIndex(getItemIndex());
		
		result.setFanTimes(getFanTimes());
		result.setUpdateTime(getUpdateTime());
		return result;
	}
}
