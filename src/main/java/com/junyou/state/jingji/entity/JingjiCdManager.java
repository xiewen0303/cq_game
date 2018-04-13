package com.junyou.state.jingji.entity;

import com.junyou.stage.model.skill.PublicCdManager;

public class JingjiCdManager extends PublicCdManager{
	private JingjiFight jingjiFight;
	public JingjiCdManager(JingjiFight jingjiFight){
		this.jingjiFight = jingjiFight;
	}
	@Override
	protected long getCurTime() {
		return jingjiFight.getCurTime();
	}
	
}
