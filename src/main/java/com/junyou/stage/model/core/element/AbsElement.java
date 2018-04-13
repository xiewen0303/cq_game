/**
 * 
 */
package com.junyou.stage.model.core.element;

import com.junyou.cmd.ClientCmdType;
import com.junyou.stage.model.core.stage.IStage;
import com.junyou.stage.model.core.stage.Point;
import com.junyou.stage.schedule.StageScheduleExecutor;

/**
 * @description
 * @author ShiJie Chi
 * @created 2011-11-30上午9:27:20
 */
public abstract class AbsElement implements IElement {
	
	private IStage stage;
	
	private Long id;
	
	private Point position;
	
	private Point bornPosition;
	
	private String name;
	
	private StageScheduleExecutor stageScheduler;
	
	/**
	 * 是否被看到  true:能,false:不能
	 */
	private boolean isBeiKuan = true;
	
	/**
	 * 是否是活动BOSS
	 */
	private boolean isActivityBoss = false;
	
	/**
	 * 
	 */
	public AbsElement(Long id,String name) {
		this.id = id;
		this.name = name;
		this.stageScheduler = new StageScheduleExecutor(id.toString());
	}
	
	@Override
	public Long getId() {
		return id;
	}

	public IStage getStage() {
		return stage;
	}

	public void setStage(IStage stage){
		this.stage = stage;
	}

	
	/**
	 * 是否被看到 
	 * @return true:能,false:不能
	 */
	public boolean isBeiKuan() {
		return isBeiKuan;
	}

	public boolean isActivityBoss(){
		return isActivityBoss;
	}
	
	/**
	 * 设置是否被看到(主要aoi通信) 
	 */
	public void setBeiKuan(boolean isBeiKuan) {
		this.isBeiKuan = isBeiKuan;
	}
	/**
	 * 设置是否是活动BOSS
	 */
	public void setActivityBoss(boolean isActivityBoss) {
		this.isActivityBoss = isActivityBoss;
	}

	@Override
	public void setPosition(int x, int y) {
		
		//记录历史坐标
		int hisX = 0;
		int hisY = 0;
		
		if(this.position != null){
			hisX = this.position.getX();
			hisY = this.position.getY();
		}
		
		//当前坐标和目标坐标是一样,不处理
		if(x != 0 &&  x == hisX && y == hisY){
			return;
		}
		
		//先更改当前坐标
		this.position = new Point(x, y);
		
		if(hisX != 0 && hisY != 0){
			//再记录历史坐标
			this.position.setHisPoint(hisX, hisY);
		}
	}
	
	/**
	 * @param
	 */
	public Point getPosition() {
		return this.position;
	}

	@Override
	public Point getBornPosition() {
		return this.bornPosition;
	}
	
	/**
	 * 设置出生点
	 * @param
	 */
	public void setBornPosition(int x,int y){
		this.bornPosition = new Point(x, y);
	}

	@Override
	public String getName() {
		return name;
	}
	
	public StageScheduleExecutor getScheduler(){
		return stageScheduler;
	}
	
	@Override
	public boolean isShowHurtRank(){
		return false;
	}
	
	/**
	 * 获取同模数据
	 * @return
	 */
	public Object getSameMsgData(Short cmd){
		return null;
	}
	
}
