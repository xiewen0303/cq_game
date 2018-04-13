package com.junyou.http.msg;


/**
 * 角色信息数据返回
 * @author LiuYu
 * @date 2015-4-27 下午4:10:12
 */
public class RoleData_Msg extends HttpCallBackMsg {
	private String userId;
	private Long roleId;
	private Integer configId;
	private String name;
	private Integer level;
	private Long createTime;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Long getRoleId() {
		return roleId;
	}
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
	public Integer getConfigId() {
		return configId;
	}
	public void setConfigId(Integer configId) {
		this.configId = configId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	public Long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}
	
}
