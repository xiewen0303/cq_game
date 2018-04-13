package com.junyou.bus.yaoshen.entity;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

@Table("role_yaoshen_fumo")
public class RoleYaoshenFumo extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("count_level")
	private Integer countLevel;

	@Column("caowei_info")
	private String caoweiInfo;

	@Column("update_time")
	private Long updateTime;

	@Column("create_time")
	private Timestamp createTime;


	//*********myself*********
	@EntityField
	private Map<Integer, Integer[]> caoweiMapInfo; //每一个槽位对应的三个格位等级信息
	public Map<Integer, Integer[]> getCaoweiMapInfo() {
		if (caoweiMapInfo != null) {
			return caoweiMapInfo;
		}
		this.caoweiMapInfo  =  JSON.parseObject(this.caoweiInfo,new TypeReference<Map<Integer, Integer[]>>(){});
		return this.caoweiMapInfo;
	}
	
	public void setCaoweiMapInfo(Map<Integer, Integer[]> caoweiMapInfo) {
		this.caoweiMapInfo = caoweiMapInfo;
	}
	public Long getUserRoleId(){
		return userRoleId;
	}

	public  void setUserRoleId(Long userRoleId){
		this.userRoleId = userRoleId;
	}

	public Integer getCountLevel(){
		return countLevel;
	}

	public  void setCountLevel(Integer countLevel){
		this.countLevel = countLevel;
	}

	public String getCaoweiInfo(){
		return caoweiInfo;
	}

	public  void setCaoweiInfo(String caoweiInfo){
		this.caoweiInfo = caoweiInfo;
	}

	public Long getUpdateTime(){
		return updateTime;
	}

	public  void setUpdateTime(Long updateTime){
		this.updateTime = updateTime;
	}

	public Timestamp getCreateTime(){
		return createTime;
	}

	public  void setCreateTime(Timestamp createTime){
		this.createTime = createTime;
	}

	@Override
	public String getPirmaryKeyName() {
		return "userRoleId";
	}

	@Override
	public Long getPrimaryKeyValue() {
		return userRoleId;
	}

	public RoleYaoshenFumo copy(){
		RoleYaoshenFumo result = new RoleYaoshenFumo();
		result.setUserRoleId(getUserRoleId());
		result.setCountLevel(getCountLevel());
		result.setCaoweiInfo(getCaoweiInfo());
		result.setUpdateTime(getUpdateTime());
		result.setCreateTime(getCreateTime());
		return result;
	}
}
