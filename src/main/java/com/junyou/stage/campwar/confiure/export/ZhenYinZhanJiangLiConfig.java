package com.junyou.stage.campwar.confiure.export;

/**
 * 
 * @description 阵营战奖励配置 
 *
 * @author LiNing
 * @date 2015-04-10 04:16:57
 */
public class ZhenYinZhanJiangLiConfig{

	private String JlItem;//奖励物品

	private Integer paiming;

	public String getJlItem() {
		return JlItem;
	}

	public void setJlItem(String jlItem) {
		JlItem = jlItem;
	}

	public Integer getPaiming() {
		return paiming;
	}

	public void setPaiming(Integer paiming) {
		this.paiming = paiming;
	}
	

	public ZhenYinZhanJiangLiConfig copy(){
		return null;
	}
}
