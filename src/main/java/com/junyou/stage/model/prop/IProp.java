package com.junyou.stage.model.prop;

import com.junyou.gameconfig.goods.configure.export.GoodsConfig;
import com.junyou.stage.model.element.role.IRole;


public interface IProp {
	
	public void add();
	
	public Object[] formatData();
	
	public String formatStr();
	
	/**
	 * 获取拥有者
	 * @return
	 */
	public IRole getOwner();
	
	
	/**
	 * 直接消耗
	 * @param costValue
	 * @return 是否已消耗完
	 */
	public boolean costValue(int costValue);
	
	public GoodsConfig getGoodsConfig();

	/**
	 * 物品ID
	 * @return
	 */
	public String getGoodsId();
	
	public int getCategory();
	/**
	 * 获取剩余时间/数值
	 * @return
	 */
	public long getRemainValue();
	
	/**
	 * 已经使用了多少
	 * @return
	 */
	public long getUseValue();
	
	/**
	 * 设置过期时间
	 * @param expireTime
	 */
	public void setExpireTime(long expireTime);
	
	/**
	 * 总共储量值
	 * @return
	 */
	public long getTotalValue();
	
	public void notifyPropChange();

	public void stop();
	
	public void end();
	
	public void start();
	
	public boolean getState();
	
	/**
	 * 延长持续
	 * @param add
	 */
	public void addValue(long add);
	
	/**
	 * 是否是同效果道具
	 * @param prop
	 * @return
	 */
	public boolean isSameProp(IProp prop);
}
