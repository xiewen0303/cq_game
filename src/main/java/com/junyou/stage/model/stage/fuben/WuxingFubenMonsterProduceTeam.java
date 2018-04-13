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
import com.junyou.utils.collection.ReadOnlyMap;
import com.kernel.gen.id.IdFactory;

/**
 * 
 *@Description 五行副本怪物生产器
 *@Author Yang Gao
 *@Since 2016-4-19 下午7:57:18
 *@Version 1.1.0
 */
public class WuxingFubenMonsterProduceTeam extends AbsElementProduceTeam {
    private StageScheduleExecutor elementScheduler;
    /*怪物是否需要回收*/
    private boolean isMonsterRetrieve;
    
    private ReadOnlyMap<String,Long> wxAttrsMap;

    public WuxingFubenMonsterProduceTeam(String teamId, ElementType elementType, int limit, String elementId, List<Integer[]> xyPoints, ReadOnlyMap<String,Long> wxAttrsMap, Integer produceDelay, Integer retrieveTime) {
        super(teamId, elementType, limit, elementId, xyPoints, produceDelay);
        this.wxAttrsMap = wxAttrsMap;
        this.elementScheduler = new StageScheduleExecutor(teamId);
        if (retrieveTime > 0) {
            isMonsterRetrieve = true;
        }
    }
    
    protected IElement create(String teamId, String elementId) {
        MonsterConfig monsterConfig = StageConfigureHelper.getMonsterExportService().load(elementId);
        if (monsterConfig == null) {
            ChuanQiLog.error("WuxingFubenMonster is null monsterId:" + elementId);
            return null;
        }
        return MonsterFactory.createWuxingFubenMonster(teamId, IdFactory.getInstance().generateNonPersistentId(), monsterConfig, wxAttrsMap, this.isMonsterRetrieve);

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