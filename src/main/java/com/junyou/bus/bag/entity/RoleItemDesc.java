package com.junyou.bus.bag.entity;
import java.io.Serializable;
import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

@Table("role_item_desc")
public class RoleItemDesc extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("bag_opening_slot")
	private Integer bagOpeningSlot;

	@Column("bag_ky_time")
	private Long bagKyTime;

	@Column("bag_update_time")
	private Long bagUpdateTime;

	@Column("storage_opening_slot")
	private Integer storageOpeningSlot;

	@Column("storage_ky_time")
	private Long storageKyTime;

	@Column("storage_update_time")
	private Long storageUpdateTime;


	public Long getUserRoleId(){
		return userRoleId;
	}

	public  void setUserRoleId(Long userRoleId){
		this.userRoleId = userRoleId;
	}

	public Integer getBagOpeningSlot(){
		return bagOpeningSlot;
	}

	public  void setBagOpeningSlot(Integer bagOpeningSlot){
		this.bagOpeningSlot = bagOpeningSlot;
	}
	/**
	 * 可用来开启格位时间
	 * @return
	 */
	public Long getBagKyTime(){
		return bagKyTime;
	}
	
	public  void setBagKyTime(Long bagKyTime){
		this.bagKyTime = bagKyTime;
	}

	public Long getBagUpdateTime(){
		return bagUpdateTime;
	}

	public  void setBagUpdateTime(Long bagUpdateTime){
		this.bagUpdateTime = bagUpdateTime;
	}

	public Integer getStorageOpeningSlot(){
		return storageOpeningSlot;
	}

	public  void setStorageOpeningSlot(Integer storageOpeningSlot){
		this.storageOpeningSlot = storageOpeningSlot;
	}

	public Long getStorageKyTime(){
		return storageKyTime;
	}

	public  void setStorageKyTime(Long storageKyTime){
		this.storageKyTime = storageKyTime;
	}

	public Long getStorageUpdateTime(){
		return storageUpdateTime;
	}

	public  void setStorageUpdateTime(Long storageUpdateTime){
		this.storageUpdateTime = storageUpdateTime;
	}

	@Override
	public String getPirmaryKeyName() {
		return "userRoleId";
	}

	@Override
	public Long getPrimaryKeyValue() {
		return userRoleId;
	}

	public RoleItemDesc copy(){
		RoleItemDesc result = new RoleItemDesc();
		result.setUserRoleId(getUserRoleId());
		result.setBagOpeningSlot(getBagOpeningSlot());
		result.setBagKyTime(getBagKyTime());
		result.setBagUpdateTime(getBagUpdateTime());
		result.setStorageOpeningSlot(getStorageOpeningSlot());
		result.setStorageKyTime(getStorageKyTime());
		result.setStorageUpdateTime(getStorageUpdateTime());
		return result;
	}
}
