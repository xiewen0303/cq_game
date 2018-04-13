/**
 *@Copyright Copyright (c) 2008 - 2100
 *@Company JunYou
 */
package com.junyou.stage.model.element.monster;

import java.util.concurrent.TimeUnit;

import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.gameconfig.monster.configure.export.MonsterConfig;
import com.junyou.stage.model.core.skill.IHarm;
import com.junyou.stage.schedule.StageTokenRunable;

/**
 * @Description
 * @Author Yang Gao
 * @Since 2016-4-19 下午8:01:46
 * @Version 1.1.0
 */
public class WuxingFubenMonster extends Monster {

    /* 生产器编号 */
    private String teamId;
    /* 五行属性类型 */
    private int monsterWxType;
    /* 是否回收 */
    private boolean isRetrieve;

    public WuxingFubenMonster(Long id, String teamId,MonsterConfig monsterConfig, boolean isRetrieve) {
        super(id, teamId, monsterConfig);
        this.teamId = teamId;
        this.isRetrieve = isRetrieve;
        this.setMonsterWxTypeVal();
    }

    public int getMonsterWxType() {
        return monsterWxType;
    }

    public void setMonsterWxTypeVal() {
        int monsterWxTypeVal = 0;
        switch (this.getMonsterType()) {
        case GameConstants.MONSTER_TYPE__WUXING_GOLD:
            monsterWxTypeVal = GameConstants.WUXING_GOLD;
            break;
        case GameConstants.MONSTER_TYPE__WUXING_WOOD:
            monsterWxTypeVal = GameConstants.WUXING_WOOD;
            break;
        case GameConstants.MONSTER_TYPE__WUXING_EARTH:
            monsterWxTypeVal = GameConstants.WUXING_EARTH;
            break;
        case GameConstants.MONSTER_TYPE__WUXING_WATER:
            monsterWxTypeVal = GameConstants.WUXING_WATER;
            break;
        case GameConstants.MONSTER_TYPE__WUXING_FIRE:
            monsterWxTypeVal = GameConstants.WUXING_FIRE;
            break;
        default:
            monsterWxTypeVal = 0;
            break;
        }
        this.monsterWxType = monsterWxTypeVal;
    }

    @Override
    protected void scheduleDisappearHandle(IHarm harm) {
        if (this.isRetrieve) {// 回收重生
            StageTokenRunable runable = new StageTokenRunable(GameConstants.DEFAULT_ROLE_ID, getStage().getId(), InnerCmdType.AI_RETRIEVE, new Object[] { getId(), getElementType().getVal(), this.teamId });
            getScheduler().schedule(getId().toString(), GameConstants.COMPONENT_AI_RETRIEVE, runable, disappearDuration, TimeUnit.SECONDS);
        } else {// 销毁
            StageTokenRunable runable = new StageTokenRunable(getId(), getStage().getId(), InnerCmdType.AI_DISAPPEAR, new Object[] { getId(), getElementType().getVal() });
            getScheduler().schedule(getId().toString(), GameConstants.COMPONENT_AI_RETRIEVE, runable, disappearDuration, TimeUnit.SECONDS);
        }
    }

}
