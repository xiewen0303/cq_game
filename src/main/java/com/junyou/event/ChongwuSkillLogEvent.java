package com.junyou.event;

import com.alibaba.fastjson.JSONArray;
import com.junyou.log.LogPrintHandle;

/**
 * 
 * @Description 宠物技能操作日志记录
 * @Author Yang Gao
 * @Since 2016-8-30
 * @Version 1.1.0
 */

public class ChongwuSkillLogEvent extends AbsGameLogEvent {

    private static final long serialVersionUID = 1L;

    private long userRoleId;// 玩家id
    private String roleName;// 玩家昵称
    private int chongwuId;// 宠物id
    private String skillId;// 宠物技能id
    private int skillIndex;// 宠物技能序号
    private int skillLevel;// 升级后宠物技能等级
    private JSONArray consumeItem;// 消耗的道具(编号:数量)
    private Integer consumeJBVal;// 操作消耗的金币数量
    private Integer consumeYBVal;// 操作消耗的元宝数量
    private Integer consumeBYBVal;// 操作消耗的绑定元宝数量

    public ChongwuSkillLogEvent(long userRoleId, String roleName, int chongwuId, String skillId, int skillIndex, int skillLevel, JSONArray consumeItem, Integer consumeJBVal, Integer consumeYBVal, Integer consumeBYBVal) {
        super(LogPrintHandle.CHONGWU_SKILL_UPLEVEL_LOG);
        this.userRoleId = userRoleId;
        this.roleName = roleName;
        this.chongwuId = chongwuId;
        this.skillId = skillId;
        this.skillIndex = skillIndex;
        this.skillLevel = skillLevel;
        this.consumeItem = consumeItem;
        this.consumeJBVal = consumeJBVal;
        this.consumeYBVal = consumeYBVal;
        this.consumeBYBVal = consumeBYBVal;
    }

    public long getUserRoleId() {
        return userRoleId;
    }

    public void setUserRoleId(long userRoleId) {
        this.userRoleId = userRoleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public int getChongwuId() {
        return chongwuId;
    }

    public void setChongwuId(int chongwuId) {
        this.chongwuId = chongwuId;
    }

    public String getSkillId() {
        return skillId;
    }

    public void setSkillId(String skillId) {
        this.skillId = skillId;
    }

    public int getSkillIndex() {
        return skillIndex;
    }

    public void setSkillIndex(int skillIndex) {
        this.skillIndex = skillIndex;
    }

    public int getSkillLevel() {
        return skillLevel;
    }

    public void setSkillLevel(int skillLevel) {
        this.skillLevel = skillLevel;
    }

    public JSONArray getConsumeItem() {
        return consumeItem;
    }

    public void setConsumeItem(JSONArray consumeItem) {
        this.consumeItem = consumeItem;
    }

    public Integer getConsumeJBVal() {
        return consumeJBVal;
    }

    public void setConsumeJBVal(Integer consumeJBVal) {
        this.consumeJBVal = consumeJBVal;
    }

    public Integer getConsumeYBVal() {
        return consumeYBVal;
    }

    public void setConsumeYBVal(Integer consumeYBVal) {
        this.consumeYBVal = consumeYBVal;
    }

    public Integer getConsumeBYBVal() {
        return consumeBYBVal;
    }

    public void setConsumeBYBVal(Integer consumeBYBVal) {
        this.consumeBYBVal = consumeBYBVal;
    }

}
