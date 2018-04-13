/**
 * 
 */
package com.junyou.stage.model.core.stage;


/**
 * @description 元素生产组
 * @author ShiJie Chi
 * @created 2011-12-16上午9:44:43
 */
public interface IElementProduceTeam {

	/**
	 * 生产
	 * @param count 指定生产数量
	 */
	public void produce(int count);
	
	/**
	 * 延迟生产(生产组某一时刻只可有一个延迟生产定时,其余忽略;延迟时间生产组内部获取)
	 */
	public void schedule();
	
	/**
	 * 重置(先{@link #IElementProduceTeam.clear()},后{@link #IElementProduceTeam.produce(getLimit)})
	 * @param
	 */
	public void reset();
	
	/**
	 * 清除
	 * @param
	 */
	public void clear();
	
	/**
	 * 回收
	 * @param
	 */
	public void retrieve(IStageElement element);
	
	/**
	 * 生产上限
	 * @param
	 */
	public int getLimit();
	
	/**
	 * 获取当前产量
	 * @param
	 */
	public int getCount();
	
	/**
	 * 获取组编号
	 * @param
	 */
	public String getId();
	
	/**
	 * 获取生产的元素类型
	 * @param
	 */
	public ElementType getElementType();
	
	/**
	 * 关联场景
	 * @param
	 */
	public void setStage(IStage stage);

	/**
	 * 生产全部
	 */
	public void produceAll();
	
	/**
	 * 随机本组的N个怪物自动巡逻
	 */
	public void randomXunlouSchedule();
	
	/**
	 * 随机获取一个组里的场景元素id
	 * @return
	 */
	public Long getRandomOneElementId();
	
	/**
	 * 是否需要延迟生产
	 * @return
	 */
	public boolean isDelayProduct();

	/**
	 * 获取生产间隔时间毫秒数
	 * @return
	 */
	public Integer getDelay();
}
