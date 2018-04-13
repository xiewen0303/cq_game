package com.junyou.bus.xiuxian.entity;
import java.io.Serializable;

import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

@Table("refb_xiuxian")
public class RefbXiuxian extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("id")
	private Long id;

	@Column("sub_id")
	private Integer subId;

	@Column("config_id")
	private Integer configId;

	@Column("buy_count")
	private Integer buyCount;

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

	public Integer getConfigId(){
		return configId;
	}

	public  void setConfigId(Integer configId){
		this.configId = configId;
	}

	public Integer getBuyCount(){
		return buyCount;
	}

	public  void setBuyCount(Integer buyCount){
		this.buyCount = buyCount;
	}

	public Long getUpdateTime(){
		return updateTime;
	}

	public  void setUpdateTime(Long updateTime){
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

	public RefbXiuxian copy(){
		RefbXiuxian result = new RefbXiuxian();
		result.setId(getId());
		result.setSubId(getSubId());
		result.setConfigId(getConfigId());
		result.setBuyCount(getBuyCount());
		result.setUpdateTime(getUpdateTime());
		return result;
	}
}
