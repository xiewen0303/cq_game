package com.junyou.stage.model.core.hatred;

import java.util.Collection;
import java.util.List;

import com.junyou.stage.model.core.element.IFighter;
import com.junyou.stage.model.core.stage.ElementType;

/**
 * @description 
 * @author ShiJie Chi
 * @date 2012-7-6 上午10:58:20 
 */
public interface IHatredRule {
	
	/**
	 * 获取最大仇恨
	 */
	public IHatred getHatredest(boolean withRefresh);
	
	/**
	 * 增加指定id仇恨
	 * @param id 
	 * @param hatredVal
	 */
	public void incrHatred(Long id,ElementType elementType,int hatredVal);
	
	/**
	 * 增加指定id仇恨
	 * @param id 
	 * @param hatredVal
	 */
	public void incrHatred(IFighter element,int hatredVal);
	
	/**
	 * 刷新仇恨
	 */
	public void refreshHatred();

	/**
	 * 设置仇恨过期时间
	 * @param duration 持续时间
	 */
	public void setHatredExpireTime(int duration);
	
	/**
	 * 设置最高仇恨可覆盖比率
	 */
	public void setHatredestReplaceRate(float rate);
	
	/**
	 * 清除所有仇恨
	 */
	public void clear();
	
	/**
	 * 获取所有仇恨
	 */
	public Collection<IHatred> getHatreds();
	
	/**
	 * 仇恨是否在激活状态(当前仇恨人数大于0)
	 */
	public boolean isActived();

	/**
	 * 最后添加的仇恨
	 */
	public IHatred getLastHatred();

	/**
	 * 获取指定目标的仇恨
	 */
	public IHatred getHatred(IFighter target);

	/**
	 * 获取指定目标的仇恨
	 */
	public IHatred getHatred(long targetRoleId);
	/**
	 * 从仇恨列表中移除目标
	 * @param targetRoleId
	 */
	public void clearHatred(long targetId);
	
	/**
	 * 获取所有仇恨目标
	 * @return
	 */
	public List<Long> getAllHaters();
}
