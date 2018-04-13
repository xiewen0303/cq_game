/**
 *@Copyright:Copyright (c) 2008 - 2100
 *@Company:JunYou
 */
package com.junyou.bus.kuafu_dianfeng.vo;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @Description 参赛玩家对象
 * @Author Yang Gao
 * @Since 2016-5-18
 * @Version 1.1.0
 */
public class DianFengPlayerVO implements Serializable {

    private static final long serialVersionUID = 6461762946900011185L;
    
    /** 竞技小组名称 **/
    private String groupName;
    /** 角色GUID **/
    private Long userRoleId;
    /** 角色配置id **/
    private Integer roleConfigId;
    /** 角色昵称 **/
    private String nickName;
    /** 角色等级 **/
    private Integer level;
    /** 角色区服id **/
    private String serverId;
    /** 角色战斗力  **/
    private Long zhanli; 
    /** 竞技结果:null=未战;true=胜利;false=失败 **/
    private Integer arenaResult;

    public DianFengPlayerVO() {
    }

    public DianFengPlayerVO(String groupName, Long userRoleId, Integer roleConfigId, String nickName, Integer level, String serverId, Long zhanli, Integer arenaResult) {
        this.groupName = groupName;
        this.userRoleId = userRoleId;
        this.roleConfigId = roleConfigId;
        this.nickName = nickName;
        this.level = level;
        this.serverId = serverId;
        this.zhanli = zhanli;
        this.arenaResult = arenaResult;
    }

    /**
     * JavaBean序列号转成Json格式数据是过滤掉这个方法
     */
    @JSONField(serialize = false)
    public Object[] getShowObject(){
        return new Object[] {
                this.getArenaResult(),
                this.getGroupName(),
                this.getUserRoleId(),
                this.getRoleConfigId(),
                this.getNickName(),
                this.getLevel(),
                this.getServerId(),
                this.getZhanli()
       };
    }
    
    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Long getUserRoleId() {
        return userRoleId;
    }

    public void setUserRoleId(Long userRoleId) {
        this.userRoleId = userRoleId;
    }

    public Integer getRoleConfigId() {
        return roleConfigId;
    }

    public void setRoleConfigId(Integer roleConfigId) {
        this.roleConfigId = roleConfigId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public Long getZhanli() {
        return zhanli;
    }

    public void setZhanli(Long zhanli) {
        this.zhanli = zhanli;
    }

    public Integer getArenaResult() {
        return arenaResult;
    }

    public void setArenaResult(Integer arenaResult) {
        this.arenaResult = arenaResult;
    }

}
