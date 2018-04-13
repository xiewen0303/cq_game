package com.junyou.bus.bag;

/**
 *@author: wind
 *@email: 18221610336@163.com
 *@version: 2014-12-22上午9:57:24
 *@Description: 
 */
public class OutputType {
	
	/**
	 *	新增物品
	 */
	public static final int ITEMVO = 1;
	/**
	 * 拆分物品
	 */
	public static final int CHAIFEN = 2;
	/**
	 * 修改数量
	 */
	public static final int MODIFY = 3;
	/**
	 * 格位变动
	 */
	public static final int SLOTMODIFY =4;
	/**
	 * 换装只变格位
	 */
	public static final int MOVESLOT = 5;
	/**
	 * roleItemExport转换成itemVo
	 */
	public static final int EXPORT_TO_ITEMVO = 6;
	/**
     * 宠物换装格位变化
     */
    public static final int CHONGWUMOVESLOT = 7;
    
    /**
     * 神器换装格位变化
     */
    public static final int SHENQIMOVESLOT = 8;

}
