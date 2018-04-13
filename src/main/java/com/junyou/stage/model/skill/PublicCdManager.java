/**
 * 
 */
package com.junyou.stage.model.skill;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.junyou.gameconfig.export.PublicCdConfig;
import com.junyou.log.ChuanQiLog;
import com.junyou.stage.configure.export.helper.StageConfigureHelper;
import com.junyou.stage.model.core.element.IFighter;
import com.junyou.utils.datetime.GameSystemTime;

/**
 * @description
 * @author ShiJie Chi
 * @created 2011-12-13上午10:36:44
 */
public class PublicCdManager implements Serializable{
	
	private static final long serialVersionUID = 1L;
	/**
	 * key:cdType val:过期时间
	 */
	private Map<String,Long> lastTriggerTimes = new HashMap<String, Long>();
	
	/**
	 * @param
	 */
	public boolean isCding(String cdType) {
	
		if(null == cdType || "".equals(cdType)){
			return false;
		}
		
		Long expireTime = lastTriggerTimes.get(cdType);
		
		if(null == expireTime || expireTime <= getCurTime()){
			return false;
		}
		
		return true;
		
	}

	/**
	 * 根据角色战斗速度动态计算cd
	 * @param cdType cd类型
	 * @param fighter 施法者
	 */
	public int toDynamicCd(String cdType, IFighter fighter) {
		
		PublicCdConfig publicCdConfig = StageConfigureHelper.getPublicCdExportService().load(cdType);
		if(null == publicCdConfig){
			return 0;
		}
		
		int standardCd = publicCdConfig.getCdTime();
		int finalCd = (int)(standardCd * (1 + fighter.getFightAttribute().getSpeed() / 1000f));
		
		toCd(cdType,finalCd);
		
		//TODO
		
		return finalCd;
		
	}
	
	/**
	 * 规则cd计算
	 * @param
	 */
	public void toCd(String cdType) {
		
		PublicCdConfig publicCdConfig = StageConfigureHelper.getPublicCdExportService().load(cdType);
		if(null != publicCdConfig){
			int standardCd = publicCdConfig.getCdTime();
			toCd(cdType,standardCd);
		}
	}
	

	private void toCd(String cdType, int finalCd) {
		lastTriggerTimes.put(cdType, getCurTime() + finalCd);
	}

	public Integer getCd(String cdType) {
		
		if(null == cdType || "".equals(cdType)){
			return 0;
		}
		
		Long expireTime = lastTriggerTimes.get(cdType);
		
		if(null != expireTime){
			int duration = (int)(expireTime - getCurTime());
			if(duration > 0){
				return duration;
			}
		}
		
		return 0;
	}
	
	protected long getCurTime(){
		return GameSystemTime.getSystemMillTime();
	}

	public Map<String, Long> getLastTriggerTimes() {
		return lastTriggerTimes;
	}

}
