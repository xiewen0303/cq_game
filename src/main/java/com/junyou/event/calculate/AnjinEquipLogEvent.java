package com.junyou.event.calculate;

import com.junyou.event.AbsGameLogEvent;
import com.junyou.log.LogPrintHandle;

/**
 * @Description 统计暗金装备日志
 * @Author Yang Gao
 * @Since 2016-7-4
 * @Version 1.1.0
 */
public class AnjinEquipLogEvent extends AbsGameLogEvent {

    private static final long serialVersionUID = 1L;

    /* 玩家等级 */
    private int roleLevel;
    /* 玩家人数 */
    private int roleCount;
    /* 装备件数 */
    private int equipCount;

    public AnjinEquipLogEvent(int roleLevel, int roleCount, int equipCount) {
        super(LogPrintHandle.CALC_ANJIN_EQUIP_LOG);
        this.roleLevel = roleLevel;
        this.roleCount = roleCount;
        this.equipCount = equipCount;
    }

    public int getRoleLevel() {
        return roleLevel;
    }

    public void setRoleLevel(int roleLevel) {
        this.roleLevel = roleLevel;
    }

    public int getRoleCount() {
        return roleCount;
    }

    public void setRoleCount(int roleCount) {
        this.roleCount = roleCount;
    }

    public int getEquipCount() {
        return equipCount;
    }

    public void setEquipCount(int equipCount) {
        this.equipCount = equipCount;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

}