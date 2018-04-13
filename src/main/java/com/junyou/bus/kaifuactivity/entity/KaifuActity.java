package com.junyou.bus.kaifuactivity.entity;
import java.io.Serializable;

import java.sql.Timestamp;

import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;
import com.kernel.check.db.annotation.*;

@Table("kaifu_actity")
public class KaifuActity extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("sub_id")
	private Integer subId;

	@Column("create_time")
	private Timestamp createTime;


	public Integer getSubId(){
		return subId;
	}

	public  void setSubId(Integer subId){
		this.subId = subId;
	}

	public Timestamp getCreateTime(){
		return createTime;
	}

	public  void setCreateTime(Timestamp createTime){
		this.createTime = createTime;
	}

	@Override
	public String getPirmaryKeyName() {
		return "subId";
	}

	@Override
	public Long getPrimaryKeyValue() {
		return (long)subId;
	}

	public KaifuActity copy(){
		KaifuActity result = new KaifuActity();
		result.setSubId(getSubId());
		result.setCreateTime(getCreateTime());
		return result;
	}
}
