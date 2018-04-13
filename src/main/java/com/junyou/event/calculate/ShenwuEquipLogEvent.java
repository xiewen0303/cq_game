package com.junyou.event.calculate;

import com.junyou.event.AbsGameLogEvent;
import com.junyou.log.LogPrintHandle;

/**
 * @Description 统计神武装备日志
 * @Author Yang Gao
 * @Since 2016-7-6
 * @Version 1.1.0
 */
public class ShenwuEquipLogEvent extends AbsGameLogEvent {

    private static final long serialVersionUID = 1L;
    /* 装备部位 */
    private int equipPart;
    /* 装备品阶 */
    private int equipQuality;
    /* 装备件数 */
    private int equipCount;

    public ShenwuEquipLogEvent(int equipPart, int equipQuality, int equipCount) {
        super(LogPrintHandle.CALC_SHENWU_EQUIP_LOG);
        this.equipPart = equipPart;
        this.equipQuality = equipQuality;
        this.equipCount = equipCount;
    }

    public int getEquipPart() {
        return equipPart;
    }

    public void setEquipPart(int equipPart) {
        this.equipPart = equipPart;
    }

    public int getEquipQuality() {
        return equipQuality;
    }

    public void setEquipQuality(int equipQuality) {
        this.equipQuality = equipQuality;
    }

    public int getEquipCount() {
        return equipCount;
    }

    public void setEquipCount(int equipCount) {
        this.equipCount = equipCount;
    }

}