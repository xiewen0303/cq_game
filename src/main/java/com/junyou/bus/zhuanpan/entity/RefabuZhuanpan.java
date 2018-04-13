package com.junyou.bus.zhuanpan.entity;
import java.io.Serializable;

import java.sql.Timestamp;

import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;
import com.kernel.check.db.annotation.*;

@Table("refabu_zhuanpan")
public class RefabuZhuanpan extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("id")
	private Long id;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("sub_id")
	private Integer subId;

	@Column("jifen")
	private Integer jifen;

	@Column("update_time")
	private Timestamp updateTime;

	@Column("create_time")
	private Timestamp createTime;

	@Column("count")
	private Integer count;

	@Column("count_update_time")
	private Timestamp countUpdateTime;


	public Long getId(){
		return id;
	}

	public  void setId(Long id){
		this.id = id;
	}

	public Long getUserRoleId(){
		return userRoleId;
	}

	public  void setUserRoleId(Long userRoleId){
		this.userRoleId = userRoleId;
	}

	public Integer getSubId(){
		return subId;
	}

	public  void setSubId(Integer subId){
		this.subId = subId;
	}

	public Integer getJifen(){
		return jifen;
	}

	public  void setJifen(Integer jifen){
		this.jifen = jifen;
	}

	public Timestamp getUpdateTime(){
		return updateTime;
	}

	public  void setUpdateTime(Timestamp updateTime){
		this.updateTime = updateTime;
	}

	public Timestamp getCreateTime(){
		return createTime;
	}

	public  void setCreateTime(Timestamp createTime){
		this.createTime = createTime;
	}

	public Integer getCount(){
		return count;
	}

	public  void setCount(Integer count){
		this.count = count;
	}

	public Timestamp getCountUpdateTime(){
		return countUpdateTime;
	}

	public  void setCountUpdateTime(Timestamp countUpdateTime){
		this.countUpdateTime = countUpdateTime;
	}

	@Override
	public String getPirmaryKeyName() {
		return "id";
	}

	@Override
	public Long getPrimaryKeyValue() {
		return id;
	}

	public RefabuZhuanpan copy(){
		RefabuZhuanpan result = new RefabuZhuanpan();
		result.setId(getId());
		result.setUserRoleId(getUserRoleId());
		result.setSubId(getSubId());
		result.setJifen(getJifen());
		result.setUpdateTime(getUpdateTime());
		result.setCreateTime(getCreateTime());
		result.setCount(getCount());
		result.setCountUpdateTime(getCountUpdateTime());
		return result;
	}
}
