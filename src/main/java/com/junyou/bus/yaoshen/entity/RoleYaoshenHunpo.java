package com.junyou.bus.yaoshen.entity;
import java.io.Serializable;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.TypeReference;
import com.kernel.check.db.annotation.Column;
import com.kernel.check.db.annotation.EntityField;
import com.kernel.check.db.annotation.Table;
import com.kernel.data.dao.AbsVersion;
import com.kernel.data.dao.IEntity;

@Table("role_yaoshen_hunpo")
public class RoleYaoshenHunpo extends AbsVersion implements Serializable,IEntity {

	@EntityField
	private static final long serialVersionUID = 1L;

	@Column("user_role_id")
	private Long userRoleId;

	@Column("jie_level")
	private Integer jieLevel;

	@Column("ceng_level")
	private Integer cengLevel;

	@Column("qnd_num")
	private Integer qndNum;

	@Column("czd_num")
	private Integer czdNum;

	@Column("tai_guang")
	private String taiGuang;

	@Column("update_time")
	private Long updateTime;


	@EntityField
	private Map<Integer, Map<Integer,String>> taiGuangMap;
	
	public Map<Integer, Map<Integer,String>> getTaiGuangMap() {
		if(taiGuangMap==null){
			if(taiGuang!=null && !taiGuang.equals("")){
				taiGuangMap = JSONArray.parseObject(this.taiGuang, new TypeReference<Map<Integer, Map<Integer,String >>>() {});
			}
		}
		return taiGuangMap;
	}
	
	public void setTaiGuangMap(Map<Integer, Map<Integer, String>> taiGuangMap) {
		this.taiGuangMap = taiGuangMap;
	}
	//taiGuangMap有变化的时候都调用这个更新实体字段
	public void updateTaiguang(){
		if(taiGuangMap!=null){
			this.taiGuang  = JSON.toJSONString(taiGuangMap);
		}
	}

	public Long getUserRoleId(){
		return userRoleId;
	}

	public  void setUserRoleId(Long userRoleId){
		this.userRoleId = userRoleId;
	}

	public Integer getJieLevel(){
		return jieLevel;
	}

	public  void setJieLevel(Integer jieLevel){
		this.jieLevel = jieLevel;
	}

	public Integer getCengLevel(){
		return cengLevel;
	}

	public  void setCengLevel(Integer cengLevel){
		this.cengLevel = cengLevel;
	}

	public Integer getQndNum(){
		return qndNum;
	}

	public  void setQndNum(Integer qndNum){
		this.qndNum = qndNum;
	}

	public Integer getCzdNum(){
		return czdNum;
	}

	public  void setCzdNum(Integer czdNum){
		this.czdNum = czdNum;
	}

	public String getTaiGuang(){
		return taiGuang;
	}

	public  void setTaiGuang(String taiGuang){
		this.taiGuang = taiGuang;
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

	public RoleYaoshenHunpo copy(){
		RoleYaoshenHunpo result = new RoleYaoshenHunpo();
		result.setUserRoleId(getUserRoleId());
		result.setJieLevel(getJieLevel());
		result.setCengLevel(getCengLevel());
		result.setQndNum(getQndNum());
		result.setCzdNum(getCzdNum());
		result.setTaiGuang(getTaiGuang());
		result.setUpdateTime(getUpdateTime());
		return result;
	}
}
