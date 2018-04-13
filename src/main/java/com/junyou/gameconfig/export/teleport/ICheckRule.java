package com.junyou.gameconfig.export.teleport;

public interface ICheckRule {

	/**
	 * 验证
	 */
	boolean check(ICheckCallback callback);

	/**
	 * 处理
	 */
	void handle(ICheckCallback callback);

}
