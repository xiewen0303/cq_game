package com.junyou.event;

/**
 * 阵营战排行榜日志
 */
import java.util.List;

import org.springframework.context.ApplicationEvent;

import com.junyou.constants.GameConstants;
import com.junyou.stage.campwar.entity.CampRank;

public class CampRankEvent extends ApplicationEvent{
	private static final long serialVersionUID = 1L;
	
 
	private List<CampRank> list ;
	public CampRankEvent(List<CampRank> list) {
		super(GameConstants.TONGYONG_EVENT_SOURCE);
		this.list = list;
	}
	public List<CampRank> getList() {
		return list;
	}
	public void setList(List<CampRank> list) {
		this.list = list;
	}
}
