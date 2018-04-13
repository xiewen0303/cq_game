package com.junyou.stage.tafang.stage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.gameconfig.export.PathInfoConfig;
import com.junyou.stage.model.core.skill.ISkill;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.core.stage.IStageElement;
import com.junyou.stage.model.core.stage.StageType;
import com.junyou.stage.model.core.stage.aoi.AOIManager;
import com.junyou.stage.model.element.monster.IMonster;
import com.junyou.stage.model.element.monster.Monster;
import com.junyou.stage.model.element.monster.ai.IAi;
import com.junyou.stage.model.element.role.Role;
import com.junyou.stage.model.stage.aoi.AoiStage;
import com.junyou.stage.model.state.AiBackState;
import com.junyou.stage.model.state.AiFightState;
import com.junyou.stage.schedule.StageScheduleExecutor;
import com.junyou.stage.schedule.StageTokenRunable;
import com.junyou.stage.tunnel.StageMsgSender;
import com.junyou.utils.datetime.GameSystemTime;


/**
 * @author LiuYu
 * 2015-10-10 上午11:36:35
 */
public class TaFangStage extends AoiStage{

	public TaFangStage(String id, Integer mapId,AOIManager aoiManager, PathInfoConfig pathInfoConfig) {
		super(id, mapId, 1, aoiManager, pathInfoConfig, StageType.TAFANG_FUBEN);
		this.scheduleExecutor = new StageScheduleExecutor(getId());
	}
	
	/**场景定时器*/
	private StageScheduleExecutor scheduleExecutor;
	/**挑战者*/
	private Role challenger;
	/**防守npc*/
	private Map<Integer,TaFangNpc> npc = new HashMap<>();
	/**塔防怪物*/
	private Map<Integer,List<TaFangMonster>> monsters = new HashMap<>();
	private TaFangComparator taFangComparator = new TaFangComparator();
	private long nextSortTime;
	private boolean start;
	private boolean fuhuo;
	
	public List<TaFangMonster> getMonsters(int line){
		List<TaFangMonster> list = monsters.get(line);
		if(list == null || list.size() < 1){
			return null;
		}
		if(nextSortTime < GameSystemTime.getSystemMillTime()){
			Collections.sort(list, taFangComparator);
			nextSortTime = GameSystemTime.getSystemMillTime() + 2000;//2秒内最多重新排序一次
		}
		return new ArrayList<>(list);
	}

	@Override
	public void enter(IStageElement element, int x, int y) {
		super.enter(element, x, y);
		if(ElementType.isRole(element.getElementType())){
			challenger = (Role)element;
			for (ISkill skill : challenger.getSkills()) {
				challenger.removeSkill(skill.getCategory());
			}
			StageMsgSender.send2One(element.getId(), ClientCmdType.TAFANG_ENTER, AppErrorCode.OK);
		}else if(ElementType.isMonster(element.getElementType())){
			Monster monster = (Monster)element;
			if(monster.isTaFangMonster()){
				TaFangMonster taFangMonster = (TaFangMonster)monster;
				int line = taFangMonster.getMoveLine();
				List<TaFangMonster> list = monsters.get(line);
				if(list == null){
					synchronized (monsters) {
						list = monsters.get(line);
						if(list == null){
							list = new ArrayList<>();
							monsters.put(line, list);
						}
					}
				}
				list.add(taFangMonster);
				taFangMonster.getStateManager().add(new AiBackState());
				taFangMonster.getAi().schedule(IAi.CRITICAL_RESPONSE_TIME, TimeUnit.MILLISECONDS);
			}else if(monster.isTaFangNpc()){
				TaFangNpc taFangNpc = (TaFangNpc)monster;
				npc.put(taFangNpc.getPositionId(), taFangNpc);
				taFangNpc.getStateManager().add(new AiFightState());
				taFangNpc.getAi().schedule(IAi.CRITICAL_RESPONSE_TIME, TimeUnit.MILLISECONDS);
			}
		}
	}

	@Override
	public void leave(IStageElement element) {
		super.leave(element);
		if(ElementType.isMonster(element.getElementType())){
			Monster monster = (Monster)element;
			if(monster.isTaFangMonster()){
				TaFangMonster taFangMonster = (TaFangMonster)monster;
				int line = taFangMonster.getMoveLine();
				List<TaFangMonster> list = monsters.get(line);
				if(list != null){
					list.remove(taFangMonster);
				}
			}else if(monster.isTaFangNpc()){
				TaFangNpc taFangNpc = (TaFangNpc)monster;
				taFangNpc.getAi().stop();
			}
		}else if(ElementType.isRole(element.getElementType())){
			challenger = null;
			StageMsgSender.send2One(element.getId(), ClientCmdType.TAFANG_EXIT, AppErrorCode.OK);
		}
	}

	public Role getChallenger() {
		return challenger;
	}

	@Override
	public boolean isCanRemove() {
		return challenger == null;
	}
	
	public void scheduleProductMonster(long time,int level,int state){
		StageTokenRunable runable = new StageTokenRunable(GameConstants.DEFAULT_ROLE_ID, getId(), InnerCmdType.TAFANG_SHUAXIN_GUAIWU, new Object[]{level,state});
		scheduleExecutor.schedule(getId(), GameConstants.COMPONENT_TAFANG_MONSTER, runable, time, TimeUnit.MILLISECONDS);
	}
	
	public void cancelSchedule(){
		scheduleExecutor.cancelSchedule(getId(), GameConstants.COMPONENT_TAFANG_MONSTER);
	}
	
	public TaFangNpc getTaFangNpc(int id){
		return npc.get(id);
	}
	
	public void start(){
		if(start){
			return;
		}
		start = true;
		//开始刷怪
		StageMsgSender.send2StageInner(GameConstants.DEFAULT_ROLE_ID, getId(), InnerCmdType.TAFANG_SHUAXIN_GUAIWU, new Object[]{1,0});
	}
	
	public void clearFubenMonster(){
		List<IStageElement> list = getAllElements(ElementType.MONSTER);
		for (IStageElement element : list) {
			IMonster monster = (IMonster)element;
			//停止定时
			monster.getScheduler().clear();
			//从副本地图中移除
			this.leave(monster);
		}
	}

	@Override
	public boolean isCanHasTangbao() {
		return false;
	}

	@Override
	public boolean isCanChangeSkill() {
		return false;
	}

    
	@Override
    public boolean canFuhuo() {
        return fuhuo;
    }
	
	public void setFuhuo(boolean fuhuo){
	    this.fuhuo = fuhuo;
	}


}
