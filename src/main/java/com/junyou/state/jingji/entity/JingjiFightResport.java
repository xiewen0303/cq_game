package com.junyou.state.jingji.entity;

import java.util.ArrayList;
import java.util.List;

import com.junyou.constants.GameConstants;

/**
 * 竞技场战报
 * @author LiuYu
 * @date 2015-3-20 上午11:37:39
 */
public class JingjiFightResport {
	List<JingjiFightRound> rounds = new ArrayList<>();
	public void addRound(JingjiFightRound round){
		rounds.add(round);
	}
	/**
	 * 获取最终战报
	 * @param time
	 * @param loser
	 * @return
	 */
	public String getResport(Long time,IJingjiFighter loser){
		StringBuffer buffer = new StringBuffer();
		for (JingjiFightRound round : rounds) {
			buffer.append(GameConstants.RESPORT_SPLIT_ONE).append(round.getTime());
			buffer.append(GameConstants.RESPORT_SPLIT_TWO).append(round.getUserRoleId());
			buffer.append(GameConstants.RESPORT_SPLIT_TWO).append(round.getSkillId());
			buffer.append(GameConstants.RESPORT_SPLIT_TWO).append(round.getHarmType());
			buffer.append(GameConstants.RESPORT_SPLIT_TWO).append(round.getHarm());
		}
		buffer.append(GameConstants.RESPORT_SPLIT_ONE).append(time);
		buffer.append(GameConstants.RESPORT_SPLIT_TWO).append(loser.getId());
		return buffer.substring(GameConstants.RESPORT_SPLIT_ONE.length());
	}
}
