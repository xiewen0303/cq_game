package com.junyou.gameconfig.publicconfig.configure.export;


/**
 * 聊天公共配置数据
 * @author DaoZheng Yuan
 * 2015年6月24日 下午6:04:53
 */
public class ChatPublicConfig extends AdapterPublicConfig{

	//最小聊天等级差
	private int level;

	/**
	 * 最小聊天等级差
	 * @return
	 */
	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
	
}
