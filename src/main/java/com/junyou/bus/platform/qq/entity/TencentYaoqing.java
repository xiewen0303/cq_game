package com.junyou.bus.platform.qq.entity;
import java.io.Serializable;

import java.sql.Timestamp;

import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;
import com.kernel.check.db.annotation.*;

@Table("tencent_yaoqing")
public class TencentYaoqing extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("id")
	private Long id;

	@Column("user_id")
	private String userId;

	@Column("iopen_id")
	private String iopenId;

	@Column("create_time")
	private Timestamp createTime;


	public Long getId(){
		return id;
	}

	public  void setId(Long id){
		this.id = id;
	}

	public String getUserId(){
		return userId;
	}

	public  void setUserId(String userId){
		this.userId = userId;
	}

	public String getIopenId(){
		return iopenId;
	}

	public  void setIopenId(String iopenId){
		this.iopenId = iopenId;
	}

	public Timestamp getCreateTime(){
		return createTime;
	}

	public  void setCreateTime(Timestamp createTime){
		this.createTime = createTime;
	}

	@Override
	public String getPirmaryKeyName() {
		return "id";
	}

	@Override
	public Long getPrimaryKeyValue() {
		return id;
	}

	public TencentYaoqing copy(){
		TencentYaoqing result = new TencentYaoqing();
		result.setId(getId());
		result.setUserId(getUserId());
		result.setIopenId(getIopenId());
		result.setCreateTime(getCreateTime());
		return result;
	}
}
