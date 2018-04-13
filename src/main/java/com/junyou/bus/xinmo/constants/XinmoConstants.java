/**
 *@Copyright:Copyright (c) 2008 - 2100
 *@Company:JunYou
 */
package com.junyou.bus.xinmo.constants;

/**
 *@Description 心魔系统常量类
 *@Author Yang Gao
 *@Since 2016-6-29
 *@Version 1.1.0
 */
public class XinmoConstants {
    
    /** 操作类型:心魔升级**/
    public static final int ACTION_TYPE_SHENGJI = 1;
    /** 操作类型:心魔突破**/
    public static final int ACTION_TYPE_TUPO = 2;
    /** 操作类型:心魔凝神**/
    public static final int ACTION_TYPE_NINGSHEN = 3;
    /** 操作类型:击杀怪物**/
    public static final int ACTION_TYPE_KILL = 4;
    /** 心魔境界前期 **/
    public static final int JINGJIE_TYPE_FIRST = 1;
    /** 心魔境界中期 **/
    public static final int JINGJIE_TYPE_MIDDLE= 2;
    /** 心魔境界后期 **/
    public static final int JINGJIE_TYPE_LAST = 3;
    /** 消耗类型:心魔境界升级 **/
    public static final int CONSUME_TYPE_SHENGJI = 1;
    /** 消耗类型:心魔境界突破 **/
    public static final int CONSUME_TYPE_TUPO= 2;
    /** 消耗类型:心魔境界凝神**/
    public static final int CONSUME_TYPE_NINGSHEN = 3;
    
    /** 初始的心魔阶级值**/
    public static final int XINMO_INIT_CATEGORY = 1;
    /** 初始的心魔时期值**/
    public static final int XINMO_INIT_TYPE= 1;
    /** 初始的心魔等级值**/
    public static final int XINMO_INIT_LEVEL = 1;
    
    
    /** 心魔凝神击杀怪物增加的心神力 **/
    public static final int MONSTER_ADD_EXP = 1;
    /** 增加心魔凝神心神力的怪物类型**/
    public static final int MONSTER_TYPE = 14;
    
    
    //---------------------------------------------------心魔:天炉炼丹-------------------------------------//
    /** 初始的心魔:天炉炼丹编号**/
    public static final int XM_LIANDAN_INIT_ID = 1;
    /** 心魔:丹药仓库最小格位号 **/
    public static final int XM_LIANDAN_MIN_SOLT = 1;
    /** 心魔:每次炼丹获得的未知丹药数量 **/
    public static final int XM_LIANDAN_DANYAN_NUM = 1;
    /** 心魔:未知丹药数量配置长度 **/
    public static final int XM_LIANDAN_CONFIG_GOODS_NUM_LEN = 2;
    
    //---------------------------------------------------心魔:魔神-------------------------------------//
    /** 初始的心魔:魔神等阶**/
    public static final int XM_MOSHEN_INIT_RANK = 0;
    //---------------------------------------------------心魔:魔神技能-------------------------------------//
    /** 心魔:魔神技能最大个数**/
    public static final int XM_MOSHEN_SKILL_MAX_NUM = 5;
    /** 心魔:魔神技能类型:普通技能**/
    public static final int XM_MOSHEN_SKILL_TYPE_NORMAL = 1;
    /** 心魔:魔神技能类型:噬体技能**/
    public static final int XM_MOSHEN_SKILL_TYPE_SHITI = 2;
    //---------------------------------------------------心魔:洗练-------------------------------------//
    /** 心魔洗练属性内容分隔符 **/
    public static final String XM_XILIAN_ATTR_SPLIT = "|";
    /** 心魔洗练的最大永久属性个数 **/
    public static final int XM_XILIAN_MAX_BASE_ATTR_NUM = 3;
    /** 心魔洗练的最大备份属性个数 **/
    public static final int XM_XILIAN_MAX_BACK_ATTR_NUM = 3;
    
    
}
