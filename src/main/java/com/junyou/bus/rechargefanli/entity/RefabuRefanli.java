package com.junyou.bus.rechargefanli.entity;
import java.io.Serializable;

import java.sql.Timestamp;

import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;
import com.kernel.check.db.annotation.*;

@Table("refabu_refanli_1")
public class RefabuRefanli extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("id")
	private Long id;
	
	@Column("user_role_id")
	private Long userRoleId;

	@Column("sub_id")
	private Integer subId;

	@Column("leiji_gold")
	private Integer leijiGold;

	@Column("fanli_gold")
	private Integer fanliGold;

	@Column("the_gold")
	private Integer theGold;

	@Column("create_time")
	private Timestamp createTime;

	@Column("update_time")
	private Timestamp updateTime;


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

	public Integer getLeijiGold(){
		return leijiGold;
	}

	public  void setLeijiGold(Integer leijiGold){
		this.leijiGold = leijiGold;
	}

	public Integer getFanliGold(){
		return fanliGold;
	}

	public  void setFanliGold(Integer fanliGold){
		this.fanliGold = fanliGold;
	}

	public Integer getTheGold(){
		return theGold;
	}

	public  void setTheGold(Integer theGold){
		this.theGold = theGold;
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
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public RefabuRefanli copy(){
		RefabuRefanli result = new RefabuRefanli();
		result.setId(getId());
		result.setUserRoleId(getUserRoleId());
		result.setSubId(getSubId());
		result.setLeijiGold(getLeijiGold());
		result.setFanliGold(getFanliGold());
		result.setTheGold(getTheGold());
		result.setCreateTime(getCreateTime());
		result.setUpdateTime(getUpdateTime());
		return result;
	}
}
