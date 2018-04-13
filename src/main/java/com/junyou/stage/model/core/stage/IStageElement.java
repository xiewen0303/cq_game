package com.junyou.stage.model.core.stage;

import com.junyou.gameconfig.export.PathNodeSize;

public interface IStageElement extends IStageElementLifeCycle{
	/**
	 * 获取ID
	 * @return
	 */
	public Long getId();
	/**
	 * 获取名字
	 * @return
	 */
	public String getName();
	/**
	 * 获取阵营
	 */
	public Integer getCamp();
	
	/**
	 * 获取占坐标大小
	 * @return
	 */
	public PathNodeSize getPathNodeSize();
	/**
	 * 获取元素类型
	 * @return
	 */
	public ElementType getElementType();
	/**
	 * 获取进入场景时的创建指令
	 * @return
	 */
	public short getEnterCommand();
	/**
	 * 设置元素所在场景
	 */
	public void setStage(IStage stage);
	/**
	 * 获取场景
	 * @return
	 */
	public IStage getStage();
	/**
	 * 设置坐标点
	 * @param x
	 * @param y
	 */
	public void setPosition(int x, int y);
	/**
	 * 设置出生点
	 * @param x
	 * @param y
	 */
	public void setBornPosition(int x, int y);
	/**
	 * 获取当前坐标点
	 */
	public Point getPosition();
	/**
	 * 获取出生点
	 */
	public Point getBornPosition();
	/**
	 * 获取占领类型
	 * @return
	 */
	public PointTakeupType getTakeupType();

	/**
	 * 消息数据
	 * @param
	 */
	public Object getMsgData();
	public Object getSameMsgData(Short cmd);
	
	/**
	 * 是否被看到 (主要aoi通信) 
	 * @return true:能,false:不能
	 */
	public boolean isBeiKuan();
	
	/**
	 * 设置是否被看到(主要aoi通信) 
	 */
	public void setBeiKuan(boolean isBeiKuan);
	/**
	 * 是否是活动BOSS
	 * @return true:是,false:否
	 */
	public boolean isActivityBoss();
	
	/**
	 * 设置是活动BOSS
	 */
	public void setActivityBoss(boolean isActivityBoss);
	
	/**
	 * 是否显示伤害排行
	 * @return
	 */
	public boolean isShowHurtRank();
}
