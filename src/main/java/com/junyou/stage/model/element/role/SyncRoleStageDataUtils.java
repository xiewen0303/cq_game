package com.junyou.stage.model.element.role;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.junyou.stage.model.core.skill.IBuff;

/**
 * @description 同步数据工具类
 * @author ChiShiJie
 * @date 2012-2-22 上午11:07:12 
 */
public class SyncRoleStageDataUtils {
	/**
	 * buffs格式化
	 */
	public static String encodeBuffs(Collection<IBuff> buffs,boolean exitGameOrNot) {
		
		if(null != buffs && buffs.size() > 0){
			
			StringBuffer result = new StringBuffer();
			
			for(IBuff buff : buffs){
				if(buff.isChangeClear()){
					continue;
				}
				if(exitGameOrNot && buff.isOfflineClear()){
					continue;
				}
				Object format = buffFormat(buff);
				result.append(format);
			}
			
			return result.toString();
			
		}
		
		return "";
	}

	/**
	 * buff格式化
	 * [category,level,starttime,attackerId,additionalData]
	 */
	private static String buffFormat(IBuff buff) {
		
		StringBuffer buffer = new StringBuffer();
		buffer.append(buff.getBuffCategory()).append(",");
		buffer.append(buff.getLevel()).append(",");
		buffer.append(buff.getStartTime()).append(",");
		buffer.append(buff.getAttackerId()).append(",");
		if(null !=buff.getAdditionalData()){
			buffer.append(buff.getAdditionalData());
		}else{
			buffer.append(" ");
		}
		
		buffer.append(";");
		
		return buffer.toString();
	}

	/**
	 * 反解buff信息
	 */
	public static List<Object[]> decodeBuffs(String buffStr) {
		
		List<Object[]> result = null;
		
		if(null != buffStr && !"".equals(buffStr)){
			
			result = new ArrayList<Object[]>();
			
			String[] buffs = buffStr.split(";");
			for(String buffInfo : buffs){
				if(!"".equals(buffInfo.trim())){
					result.add(buffInfo.split(","));
				}
					
			}
		}
		
		return result;
	}

}
