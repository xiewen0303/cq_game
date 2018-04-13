package com.junyou.bus.happycard.entity;

import java.io.Serializable;

import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

@Table("refabu_happy_card_item")
public class RefabuHappyCardItem extends AbsVersion implements Serializable,
		IEntity, Comparable<RefabuHappyCardItem> {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("id")
	private Long id;

	@Column("sub_id")
	private Integer subId;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("items")
	private String items;

	@Column("item_index")
	private Integer itemIndex;

	@Column("update_time")
	private Long updateTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getSubId() {
		return subId;
	}

	public void setSubId(Integer subId) {
		this.subId = subId;
	}

	public Long getUserRoleId() {
		return userRoleId;
	}

	public void setUserRoleId(Long userRoleId) {
		this.userRoleId = userRoleId;
	}

	public String getItems() {
		return items;
	}

	public void setItems(String items) {
		this.items = items;
	}

	public Long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}

	@Override
	public String getPirmaryKeyName() {
		return "id";
	}

	@Override
	public Long getPrimaryKeyValue() {
		return id;
	}

	public Integer getItemIndex() {
		return itemIndex;
	}

	public void setItemIndex(Integer itemIndex) {
		this.itemIndex = itemIndex;
	}

	public RefabuHappyCardItem copy() {
		RefabuHappyCardItem result = new RefabuHappyCardItem();
		result.setId(getId());
		result.setSubId(getSubId());
		result.setUserRoleId(getUserRoleId());
		result.setItems(getItems());
		result.setItemIndex(getItemIndex());
		result.setUpdateTime(getUpdateTime());
		return result;
	}

	@Override
	public int compareTo(RefabuHappyCardItem o) {
		if (this.updateTime < o.updateTime) {
			return -1;
		} else {
			return 1;
		}
	}
}
