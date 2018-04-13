package com.junyou.utils.number;

import java.text.DecimalFormat;

/**
 * 小数工具类
 *@author  DaoZheng Yuan
 *@created 2012-5-18下午6:35:30
 */
public class NumberUtil {

	
	/**
	 * 保留两位小数
	 * @param numberValue
	 * @return
	 */
	public static Float getOneDigitsFloat(Float numberValue){
		DecimalFormat df = new DecimalFormat("#.0");
	    String tmp = df.format(numberValue);
	    
	    return Float.parseFloat(tmp);
	}
	
}
