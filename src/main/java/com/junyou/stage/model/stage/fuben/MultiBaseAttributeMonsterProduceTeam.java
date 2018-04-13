package com.junyou.stage.model.stage.fuben;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.gameconfig.monster.configure.export.MonsterConfig;
import com.junyou.log.ChuanQiLog;
import com.junyou.stage.configure.export.helper.StageConfigureHelper;
import com.junyou.stage.model.core.element.IElement;
import com.junyou.stage.model.core.stage.AbsElementProduceTeam;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.element.monster.MonsterFactory;
import com.junyou.stage.schedule.StageScheduleExecutor;
import com.junyou.stage.schedule.StageTokenRunable;
import com.kernel.gen.id.IdFactory;

/**
 *@Description 支持生产扩大基础属性(倍率*7大基本属性)的怪物生产器
 *@Author Yang Gao
 *@Since 2016-8-8
 *@Version 1.1.0
 */
public class MultiBaseAttributeMonsterProduceTeam extends AbsElementProduceTeam {
    private StageScheduleExecutor elementScheduler;
    /*需要翻倍的属性类型集合*/
    List<String> multiAttrList;
    /*倍率因子*/
    private float multiplier;

    public MultiBaseAttributeMonsterProduceTeam(String teamId, ElementType elementType, int limit, String elementId, List<Integer[]> xyPoints, Integer produceDelay, List<String> multiAttrList, float multiplier) {
        super(teamId, elementType, limit, elementId, xyPoints, produceDelay);
        this.multiplier = multiplier;
        this.multiAttrList = multiAttrList;
        this.elementScheduler = new StageScheduleExecutor(teamId);
    }
    
    protected IElement create(String teamId, String elementId) {
        MonsterConfig monsterConfig = StageConfigureHelper.getMonsterExportService().load(elementId);
        if (monsterConfig == null) {
            ChuanQiLog.error("MultiBaseAttributeMonsterProduceTeam is null monsterId:" + elementId);
            return null;
        }
        return MonsterFactory.createMultiFubenMonster(IdFactory.getInstance().generateNonPersistentId(), monsterConfig, this.multiAttrList, this.multiplier);
    }
    
    private String getScheduleId(){
        return new StringBuilder().append(stage.getId()).append("_").append(teamId).toString();
    }
    
    @Override
    public void schedule() {
        StageTokenRunable runable = new StageTokenRunable(null, stage.getId(), InnerCmdType.AI_PRODUCE, new Object[] { stage.getId(), teamId, elementType.getVal() });
        elementScheduler.schedule(getScheduleId(), GameConstants.COMPONENT_PRODUCE_TEAM, runable, getDelay(), TimeUnit.MILLISECONDS);
    }

    public void clearSchedule(){
        elementScheduler.clear();
    }
    
    @Override
    public void randomXunlouSchedule() {

    }

    @Override
    public Long getRandomOneElementId() {
        return null;
    }

    @Override
    public boolean isDelayProduct() {
        return super.getDelay() > 0;
    }

}