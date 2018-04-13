package com.junyou.bus.kuafuarena1v1.constants;

public class KuafuArena1v1Constants {
	/**
	 * 每日奖励功勋状态：不能領取
	 */
	public static final int GONGXUN_STATUS_0 = 0;
	/**
	 * 每日奖励功勋状态：可領取
	 */
	public static final int GONGXUN_STATUS_1 = 1;
	/**
	 * 每日奖励功勋状态：已领取
	 */
	public static final int GONGXUN_STATUS_2 = 2;

	public static final String STAGE_ID_PREFIX = "kuafu_arena_stage_";
	
	/**
	 * 正在匹配中
	 */
	public static final int STATUS_1 = 1;
	/**
	 * 匹配成功，还未开始pk
	 */
	public static final int STATUS_2 = 2;
	/**
	 * 正在pk中
	 */
	public static final int STATUS_3 = 3;
	/**
	 * pk结束后未退出
	 */
	public static final int STATUS_4 = 4;
	/**
	 * 超时
	 */
	public static final int MATCH_FAIL_REASON_1 = 1;
	/**
	 * 未获得锁
	 */
	public static final int MATCH_FAIL_REASON_2 = 2;
}
