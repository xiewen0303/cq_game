package com.junyou.bus.lunpan.entity;
import java.io.Serializable;

import java.sql.Timestamp;

import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;
import com.kernel.check.db.annotation.*;

@Table("refabu_lunpan")
public class RefabuLunpan extends AbsVersion implements Serializable,IEntity {

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

	@Column("count")
	private Integer count;

	@Column("gold")
	private Integer gold;

	@Column("create_time")
	private Timestamp createTime;

	@Column("update_time")
	private Timestamp updateTime;


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

	public Integer getCount(){
		return count;
	}

	public  void setCount(Integer count){
		this.count = count;
	}

	public Integer getGold(){
		return gold;
	}

	public  void setGold(Integer gold){
		this.gold = gold;
	}

	public Timestamp getCreateTime(){
		return createTime;
	}

	public  void setCreateTime(Timestamp createTime){
		this.createTime = createTime;
	}

	public Timestamp getUpdateTime(){
		return updateTime;
	}

	public  void setUpdateTime(Timestamp updateTime){
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

	public RefabuLunpan copy(){
		RefabuLunpan result = new RefabuLunpan();
		result.setId(getId());
		result.setUserRoleId(getUserRoleId());
		result.setSubId(getSubId());
		result.setJifen(getJifen());
		result.setCount(getCount());
		result.setGold(getGold());
		result.setCreateTime(getCreateTime());
		result.setUpdateTime(getUpdateTime());
		return result;
	}
}
