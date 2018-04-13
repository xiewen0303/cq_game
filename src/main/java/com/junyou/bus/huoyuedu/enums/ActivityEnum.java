package com.junyou.bus.huoyuedu.enums;
/** 
 *注意：这里不要用格式化代码！  thx;
 *各种活动（任务）对应的类型
 * @author lxn
 *活跃度id + 活动描述
 */
public enum ActivityEnum {
	/**
	 * 微端登录游戏
	 */
    A1(1),
    /**
	 * 强化装备1次
	 */
    A2(2),
    /**
   	 * 御剑进阶1次
   	 */
    A3(3),
	/**
	 * 翅膀进阶1次
	 */
    A4(4),
	/**
	 * 天工进阶1次
	 */
    A5(5),
	/**
	 *  天裳进阶1次
	 */
    A6(6),
	/**
	 *  竞技场挑战5次
	 */
    A7(7),
	/**
	 * 荣誉兑换1次
	 */
    A8(8),
	/**
	 *  熔炼1件装备
	 */
    A9(9),
	/**
	 * 玄铁兑换1次
	 */
    A10(10),
	/**
	 *  元宝消费1次
	 */
    A11(11),
	/**
	 *  绑元消费1次
	 */
    A12(12),
	/**
	 *  Vip5自动赠送
	 */
    A13(13),
	/**
	 *  完成日常任务
	 */
    A14(14),
	/**
	 *  完成门派任务
	 */
    A15(15),
	/**
	 * 参与1次花千骨副本
	 */
    A16(16),
	/**
	 *  参与1次野外BOSS
	 */
    A17(17),
	/**
	 * 参与1次幻境历练
	 */
    A18(18),
	/**
	 *  参与1次南无月副本
	 */
    A19(19),
	/**
	 *  参与1次仙魔榜副本
	 */
    A20(20),
	/**
	 *  参与1次答题活动
	 */
    A21(21),
	/**
	 * 参与1次魔宫寻宝
	 */
    A22(22),
	/**
	 * 参与1次古神战场
	 */
    A23(23),
	/**
	 *  护送1次美女
	 */
    A24(24),
	/**
	 * 成功劫镖1次
	 */
    A25(25),
    	/**
	 * 参与1次八卦阵副本
	 */
    A26(26),
       /**
	 * 参与1次埋骨之地副本
	 */
    A27(27),
    /**
   	 * 新圣剑进阶
   	 */
    A28(28);
    
    private  int activityId;
	private ActivityEnum(int activityId){
		this.activityId = activityId;
	}
	public Integer getActivityId() {
		return activityId;
	}
	public static ActivityEnum getEnumById(int id){
		for (ActivityEnum acEnum : ActivityEnum.values()) {
			if(acEnum.getActivityId()==id){
				return acEnum;
			}
		}
		return null;
	}
}
