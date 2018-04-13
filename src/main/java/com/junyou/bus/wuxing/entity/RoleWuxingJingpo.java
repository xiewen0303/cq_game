package com.junyou.bus.wuxing.entity;
import java.io.Serializable;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.TypeReference;
import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

@Table("role_wuxing_jingpo")
public class RoleWuxingJingpo extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("body_data")
	private String bodyData;

	@Column("open_slot")
	private Integer openSlot;

	@Column("moshen_jinghua")
	private Long moshenJinghua;

	@Column("lieming_level")
	private Integer liemingLevel;

	@Column("create_time")
	private Long createTime;

	@Column("update_time")
	private Long updateTime;

    @EntityField
    private Map<Integer, Object[]> bodyJingpoMap;
    
    public Map<Integer, Object[]> getBodyJingpoMap() {
        if(null == bodyJingpoMap){
            if(null != bodyData && !"".equals(bodyData)){
                bodyJingpoMap = JSONArray.parseObject(bodyData, new TypeReference<Map<Integer, Object[]>>(){});
            }
        }
        return bodyJingpoMap;
    }
    
	public Long getUserRoleId(){
		return userRoleId;
	}

	public  void setUserRoleId(Long userRoleId){
		this.userRoleId = userRoleId;
	}

	public String getBodyData(){
		return bodyData;
	}

	public  void setBodyData(String bodyData){
		this.bodyData = bodyData;
	}

	public Integer getOpenSlot(){
		return openSlot;
	}

	public  void setOpenSlot(Integer openSlot){
		this.openSlot = openSlot;
	}

	public Long getMoshenJinghua(){
		return moshenJinghua;
	}

	public  void setMoshenJinghua(Long moshenJinghua){
		this.moshenJinghua = moshenJinghua;
	}

	public Integer getLiemingLevel(){
		return liemingLevel;
	}

	public  void setLiemingLevel(Integer liemingLevel){
		this.liemingLevel = liemingLevel;
	}

	public Long getCreateTime(){
		return createTime;
	}

	public  void setCreateTime(Long createTime){
		this.createTime = createTime;
	}

	public Long getUpdateTime(){
		return updateTime;
	}

	public  void setUpdateTime(Long updateTime){
		this.updateTime = updateTime;
	}

	@Override
	public String getPirmaryKeyName() {
		return "userRoleId";
	}

	@Override
	public Long getPrimaryKeyValue() {
		return userRoleId;
	}

	public RoleWuxingJingpo copy(){
		RoleWuxingJingpo result = new RoleWuxingJingpo();
		result.setUserRoleId(getUserRoleId());
		result.setBodyData(getBodyData());
		result.setOpenSlot(getOpenSlot());
		result.setMoshenJinghua(getMoshenJinghua());
		result.setLiemingLevel(getLiemingLevel());
		result.setCreateTime(getCreateTime());
		result.setUpdateTime(getUpdateTime());
		return result;
	}
	
}
