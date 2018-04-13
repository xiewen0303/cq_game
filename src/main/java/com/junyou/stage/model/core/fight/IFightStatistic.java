/**
 * 
 */
package com.junyou.stage.model.core.fight;

import com.junyou.stage.model.core.skill.IBuff;
import com.junyou.stage.model.core.skill.IHarm;
import com.junyou.stage.model.core.state.IState;
import com.junyou.stage.tunnel.IMsgWriter;


/**
 * @description 战斗统计<br/>
 * 1、收集战斗过程中相关属性变化，将不同属性变化信息传送到需要监听的模块以及客户端<br/>
 * 2、可以在战斗过程完成后，再一起刷出战斗过程中涉及到的属性变化。
 * @author ShiJie Chi
 * @created 2011-12-6下午2:11:19
 */
public interface IFightStatistic {

	/**
	 * 输出战斗中相关变化(属性，状态......)
	 * @param
	 */
	void flushChanges(IMsgWriter msgWriter);

	/**
	 * @param
	 */
	void addHarm(IHarm harm);
	
	/**
	 * 设置属性变化
	 * @param
	 */
	public void setAttribute(Integer index, Object val);
	
	/**
	 * 设置 hp发生变化
	 */
	public void hpChange();

	
	/**
	 * 设置 mp发生变化
	 */
	public void mpChange();
	
	/**
	 * 设置真元发生变化
	 */
	public void zyChange();

	/**
	 * 死亡
	 */
	void dead();
	
	/**
	 * 移动速度变化
	 */
	public void speedChange();

	/**
	 * 移除buff
	 */
	void removeBuff(IBuff buff);

	/**
	 * 增加buff
	 */
	void addBuff(IBuff buff);

	/**
	 * 增加战斗状态
	 */
	void addState(IState state);
	
	/**
	 * 状态被覆盖
	 */
	void replaceState(IState state);

	/**
	 * 状态被清理
	 */
	void removeState(IState state);

	/**
	 * 获取进入战斗状态时间
	 * @return
	 */
	Long getInFightTime();
}
