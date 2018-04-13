package com.junyou.stage.model.skill.buff;

import java.util.ArrayList;
import java.util.List;

import com.junyou.stage.JunQinStateConstants;
import com.junyou.stage.OnlineStateConstants;

/**
 * BUFF 过滤 工具
 *@author  DaoZheng Yuan
 *@created 2012-11-21上午9:44:16
 */
public class BuffRecoverIgnoreUil {

	private static List<String> buffFilters = new ArrayList<String>();
	static{
		//和平BUFF
		buffFilters.add(OnlineStateConstants.ONLINE_BUFF_ID);
		//免战BUFF
		buffFilters.add(JunQinStateConstants.JUNQIN_BUFF_ID);
	}
	
	/**
	 * 是否包含需要过滤的BUFF
	 * @param checkBuffId
	 * @return true:包含
	 */
	public static boolean contains(String checkBuffId){
		return buffFilters.contains(checkBuffId);
	}
	
}
