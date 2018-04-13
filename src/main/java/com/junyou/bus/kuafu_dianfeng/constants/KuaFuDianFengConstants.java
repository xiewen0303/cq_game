package com.junyou.bus.kuafu_dianfeng.constants;

/**
 *@Description  巅峰之战常量字段类
 *@Author Yang Gao
 *@Since 2016-5-18
 *@Version 1.1.0
 */
public class KuaFuDianFengConstants {    
    
    /** 第一轮数 **/
    public static final int LOOP_ONE = 1;
    /** 第二轮数 **/
    public static final int LOOP_TWO = 2;
    /** 第三轮数 **/
    public static final int LOOP_THREE = 3;
    /** 第四轮数 **/
    public static final int LOOP_FOUR = 4;
    
    /** 第一轮生成的最大房间数 **/
    public static final int MAX_ROOM_LOOP_ONE = 8;
    /** 第二轮生成的最大房间数 **/
    public static final int MAX_ROOM_LOOP_TWO = 4;
    /** 第三轮生成的最大房间数 **/
    public static final int MAX_ROOM_LOOP_THREE = 2;
    /** 第四轮生成的最大房间数 **/
    public static final int MAX_ROOM_LOOP_FOUR = 1;	
    
    /** 每轮比赛结果:小场胜利  **/
    public static final int RESULT_TYPE_WIN = 1;
    /** 每轮比赛结果:小场失败  **/
    public static final int RESULT_TYPE_FAIL = 2;
    /** 每轮比赛结果:最终胜利 **/
    public static final int RESULT_TYPE_FINAL_WIN = 3;
    
    /** 战力结果相同情况下,双方获胜的概率均占50% **/
    public static final Float ZHANLI_RATE = 0.5F;
    
    /** 巅峰之战参赛选手战斗结果状态:尚未出结果  **/
    public static final int STATE_NOT = 0;
    /** 巅峰之战参赛选手战斗结果状态:获胜结果  **/
    public static final int STATE_WIN = 1;
    /** 巅峰之战参赛选手战斗结果状态:失败结果  **/
    public static final int STATE_LOSE = 2;
    
    
}
