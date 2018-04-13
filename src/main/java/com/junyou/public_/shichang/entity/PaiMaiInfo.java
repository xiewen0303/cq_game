package com.junyou.public_.shichang.entity;
 
import java.io.Serializable;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;
import com.kernel.check.db.annotation.*;

@Table("pai_mai_info")
public class PaiMaiInfo extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("guid")
	private Long guid;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("price")
	private Integer price;

	@Column("role_name")
	private String roleName;

	@Column("sell_time")
	private Long sellTime;

	@Column("sell_type")
	private Integer sellType;


	public Long getGuid(){
		return guid;
	}

	public  void setGuid(Long guid){
		this.guid = guid;
	}

	public Long getUserRoleId(){
		return userRoleId;
	}

	public  void setUserRoleId(Long userRoleId){
		this.userRoleId = userRoleId;
	}

	public Integer getPrice(){
		return price;
	}

	public  void setPrice(Integer price){
		this.price = price;
	}

	public String getRoleName(){
		return roleName;
	}

	public  void setRoleName(String roleName){
		this.roleName = roleName;
	}

	public Long getSellTime(){
		return sellTime;
	}

	public  void setSellTime(Long sellTime){
		this.sellTime = sellTime;
	}

	public Integer getSellType(){
		return sellType;
	}

	public  void setSellType(Integer sellType){
		this.sellType = sellType;
	}

	@Override
	public String getPirmaryKeyName() {
		return "guid";
	}

	@Override
	public Long getPrimaryKeyValue() {
		return guid;
	}

	public PaiMaiInfo copy(){
		PaiMaiInfo result = new PaiMaiInfo();
		result.setGuid(getGuid());
		result.setUserRoleId(getUserRoleId());
		result.setPrice(getPrice());
		result.setRoleName(getRoleName());
		result.setSellTime(getSellTime());
		result.setSellType(getSellType());
		return result;
	}
}
