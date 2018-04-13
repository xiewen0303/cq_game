package com.junyou.start;


/**
 * 主入口
 * @description 服务器启动入口
 */
public class ChuanQiStart {

	public static void main(String[] args) {
		//jdk 7 sort排序算法变了，对原有1.7以下的排序代码兼容
		System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
		GameBootService boot = new GameBootService(args);
		boot.start();
	}
}
