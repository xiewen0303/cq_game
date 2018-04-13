package com.junyou.stage.campwar.confiure.export;


/**
 * 
 * @description 阵营战经验配置 
 *
 * @author LiNing
 * @date 2015-04-10 14:57:19
 */
public class ZhenYinZhanJingYanConfig{

	private Long exp;

	private Integer level;
	
	public Long getExp() {
		return exp;
	}

	public void setExp(Long exp) {
		this.exp = exp;
	}
	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}
	
	public ZhenYinZhanJingYanConfig copy(){
		return null;
	}
}
