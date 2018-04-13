package com.kernel.gen.id;

/**
 * 最大值为15 除了普通类型外其他的都是场景中需要持久化的战斗单位需要特别申明类型
 * 
 * @author ydz
 */
public class ServerIdType {
	
	/** 通用的,大部分对象都可以用于这个 */
	public final static byte COMMON = 0;
	/** 角色 */
	public final static byte ROLE = 1;
	/** 宠物 */
	public final static byte PET = 2;
	/** 坐骑 */
	public final static byte ZUOQI = 3;
	/** 物品 */
	public final static byte GOODS = 4;
	/** 物品相关 */
	public final static byte GOODS_OTHER = 5;
	/** 充值 */
	public final static byte RECHARGE = 7;
	/** 副本相关 */
	public final static byte FUBEN = 9;
	/** 竞技场相关 */
	public final static byte JINGJI = 10;
	/** 兑换卡*/
	public final static byte GIFTCARD = 11;
	/** 寻宝*/
	public final static byte XUNBAO_TEMP_GUID = 12;
	/** 热发布物品*/
	public final static byte RFB_GOODS = 13;
	/** 门派相关*/
	public final static byte GUILD = 14;
	/** 热发布活动相关*/
	public final static byte REFABU = 15;
	
}
