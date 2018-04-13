/**
 * 
 */
package com.junyou.stage.model.core.stage;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.junyou.gameconfig.export.PathNodeSize;

/**
 * @description aoi场景
 * @author ShiJie Chi
 * @created 2011-11-25下午1:48:39
 */
public interface IStage{

	/**
	 * 获取场景id
	 * @param
	 */
	public String getId();

	/**
	 * 获取地图id
	 */
	public Integer getMapId();

	/**
	 * 获取分线号
	 */
	public Integer getLineNo();
	
	/**
	 * 验证坐标点合法性
	 * @param
	 */
	public boolean checkPoint(Point point);
	
	/**
	 * 验证坐标是否可用[指定的点不可用]
	 * @param point
	 * @param takeupType
	 * @return true;
	 */
	public boolean checkCanUseStagePoint(Point point,PointTakeupType takeupType);
	/**
	 * 验证坐标是否可用[指定的点不可用]
	 * @param x
	 * @param y
	 * @param takeupType
	 * @return
	 */
	public boolean checkCanUseStagePoint(int x,int y,PointTakeupType takeupType);
	
	/**
	 * 验证坐标是否可用[人和物品的点都不可用]
	 * @param point
	 * @param takeupType
	 * @return true;
	 */
	public boolean checkCanUseStagePoint(Point point);
	
	/**
	 * 点是否是可走点
	 * @param x
	 * @param y
	 * @return true:是
	 */
	public boolean isCanUseStagePoint(int x,int y);
	
	/**
	 * 是否是不可攻击点
	 * @param x
	 * @param y 
	 * @return true:是
	 */
	public boolean isNoAttackePoint(int x,int y);

	/**
	 * 是否在视野内
	 * @param from 发起方
	 * @param target 目标
	 */
	public boolean inSight(IStageElement from, IStageElement target);
	
	/**
	 * 是否在视野内
	 * @param from 发起方
	 * @param targetPoint 目标点
	 */
	public boolean inSight(IStageElement from, Point targetPoint);
	
	/**
	 * 是否在指定范围内
	 */
	public boolean inScope(Point from,Point target,int step,ScopeType scopeType);
	
	/**
	 * 是否在指定格子内
	 * @param from
	 * @param target
	 * @param step
	 * @return true:在
	 */
	public boolean isScopeGeZi(Point from, Point target,int step);
	
	/**
	 * 是否在指定格子内
	 * @param from
	 * @param tx
	 * @param ty
	 * @param step
	 * @return true:在
	 */
	public boolean isScopeGeZi(Point from,int tx,int ty,int step);

	/**
	 * 占领某个点
	 */
	public void taskupPoint(Point position, boolean b, PointTakeupType takeipType, PathNodeSize pathNodeSize);

	/**
	 * 获取指定坐标点附近可用坐标点
	 * @param priorOrNot 指定优先
	 * @param direction 
	 */
	public Point getSurroundValidPoint(Point point,boolean priorOrNot, PointTakeupType takeupType);

	/**
	 * 进入场景
	 * @param
	 */
	public void enter(IStageElement element, int x, int y);

	/**
	 * 离开场景
	 * @param
	 */
	public void leave(IStageElement element);
	
	/**
	 * 获取场景内人数
	 * @return
	 */
	public int getStageRoleCount();
	
	/**
	 * 移动到指定坐标
	 * @param
	 */
	public void moveTo(IStageElement element, int x, int y);

	/**
	 * 瞬移到指定坐标
	 * @param
	 */
	public void teleportTo(IStageElement element, int x, int y);
	
	/**
	 * 根据指定类型获取元素
	 * @param
	 */
	public  <T extends IStageElement> T  getElement(Long elementId, ElementType elementType);
	
	/**
	 * 获取周围敌人
	 */
	public Collection<IStageElement> getAroundEnemies(IStageElement role);
	
	/**
	 * 获取周围的红名玩家
	 * @param fighter
	 * @return
	 */
	public Collection<IStageElement> getAroundRedRoles(IStageElement fighter);

	
	/**
	 * 获取指定类型的周边元素
	 * @param from 起始坐标点
	 * @param filter 元素搜索过滤器
	 */
	public <T extends IStageElement> Collection<T> getSurroundElements(Point from, ElementType elementType, IElementSearchFilter filter);

	/**
	 * 获取指定类型的周边元素
	 * @param from 起始坐标点
	 */
	public <T extends IStageElement> Collection<T> getSurroundElements(Point from, ElementType elementType);
	
	/**
	 * 获取周边元素
	 */
	public List<Long> getSurroundElementIds(Point from,ElementType elementType,IElementSearchFilter filter);
	
	/**
	 * 获取周边的玩家ids
	 */
	public Long[] getSurroundRoleIds(Point from, IElementSearchFilter filter);
	
	/**
	 * 获取周边的玩家ids
	 */
	public Long[] getSurroundRoleIds(Point from);
	
	/**
	 * 获取当前场景的所有角色
	 * @return
	 */
	public Map<Long, IStageElement> getBaseStageRoles();
	
	/**
	 * 获取周边的非自己玩家ids
	 * @param from
	 * @param selfId
	 * @return
	 */
	public Long[] getNoSelfSurroundRoleIds(Point from,Long selfId);

	/**
	 * 生产管理器
	 */
	public IStageProduceManager getStageProduceManager();
	
	/**
	 * 设置场景产生管理器
	 */
	public void setStageProduceManager(IStageProduceManager stageProduceManager);

	/**
	 * 判断是否为副本创建
	 * @return true 是
	 */
	public boolean isCopy();
	
	/**
	 * 获取场景类型
	 * @return
	 */
	public StageType getStageType();
	
	/**
	 * 是否可以道具复活
	 * @return
	 */
	public boolean canFuhuo();
	
	/**
	 * 是否可以回收
	 * @return
	 */
	public boolean isCanRemove();
	
	/**
	 * 场景是否可以PK
	 * @return
	 */
	public boolean isCanPk();
	
	/**
	 * 是否增加PK值
	 * @return
	 */
	public boolean isAddPk();
	
	/**
	 * 是否可以上飞剑
	 * @return
	 */
	public boolean isCanFeijian();
	
	/**
	 * 是否可以打坐
	 * @return
	 */
	public boolean isCanDazuo();
	
	/**
	 * 是否可以跳
	 * @return
	 */
	public boolean isCanJump();
	
	/**
	 * 获取同模指令
	 * @return
	 */
	public Short getSameMoCmd();
	
	/**
	 * 是否可以携带糖宝
	 * @return
	 */
	public boolean isCanHasTangbao();
	/**
	 * 是否可以携带宠物
	 * @return
	 */
	public boolean isCanHasChongwu();
	
	/**
	 * 获取回城复活内部指令
	 * @return
	 */
	public Short getBackFuHuoCmd();
	/**
	 * 获取角色死亡强制回城复活时间
	 * @return
	 */
	public Integer getQzFuhuoSecond();
	/**
	 * 角色死亡强制回城复活票据常量
	 * @return
	 */
	public String getBackFuhuoConstants();
	
	/**
	 * 获取场景内玩家id集合
	 * @return
	 */
	public Object[] getAllRoleIds();
	
	/**
	 * 是否可以使用道具
	 * @return
	 */
	public boolean isCanUseProp();
	
	/**
	 * 是否可以使用神器
	 * @return
	 */
	public boolean isCanUseShenQi();
	/**
	 * 设置旗子
	 */
	public void setFlag(boolean flag);
	/**
	 * 是否有的旗子
	 * @return
	 */
	public boolean hasFlag();
	
	/**
	 * 是否可切换妖神、普通技能
	 * @return
	 */
	public boolean isCanChangeSkill();
	/**
	 * 能够变换妖神
	 * @return
	 */
	public boolean isCanChangeYaoShen();
	/**
	 * 能够变换外显
	 * @return
	 */
	public boolean isCanChangeShow();
	
	/**
	 * 怪物死亡展示
	 * @return
	 */
	public DeadDisplay getDeadHiddenState();
	
	public Point getCanMovePosition(Point attackerPostion, Point targetPoint);
	
	/**
	 * 判断人物是否可以移动点
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean checkCanMoveStagePoint(int x, int y);
}
